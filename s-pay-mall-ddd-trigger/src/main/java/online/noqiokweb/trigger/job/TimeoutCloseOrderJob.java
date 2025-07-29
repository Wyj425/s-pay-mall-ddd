package online.noqiokweb.trigger.job;

import lombok.extern.slf4j.Slf4j;
import online.noqiokweb.domain.order.service.IOrderService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


import javax.annotation.Resource;
import java.util.List;

/**
 * @author TheLastSavior noqiokweb.site @wyj
 * @description 超时关单
 * @create 7/4/2025 11:10 PM
 */
@Slf4j
@Component
public class TimeoutCloseOrderJob {
    @Resource
    private IOrderService orderService;

    @Scheduled(cron = "0 0/30 * * * ?")
    public void exec(){
        try {
            log.info("任务：超时30分钟订单关闭");
            List<String> orderIds=orderService.queryTimeoutCloseOrderList();
            if (null == orderIds || orderIds.isEmpty()){
                log.info("无超时订单");
                return;
            }
            for(String orderId : orderIds){
                boolean status=orderService.changeOrderClose(orderId);
                log.info("订单：{} 关闭状态：{}", orderId, status);
            }
        } catch (Exception e) {
            log.error("超时30分钟订单关闭失败", e);
        }
    }
}
