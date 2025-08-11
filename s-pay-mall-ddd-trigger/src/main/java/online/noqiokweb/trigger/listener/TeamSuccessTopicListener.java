package online.noqiokweb.trigger.listener;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import online.noqiokweb.api.dto.NotifyRequestDTO;
import online.noqiokweb.domain.order.service.OrderService;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author Fuzhengwei bugstack.cn @小傅哥
 * @description 结算完成消息监听
 * @create 2025-03-08 13:49
 */
@Slf4j
@Component
public class TeamSuccessTopicListener {
    @Resource
    private OrderService orderService;

    /**
     * 指定消费队列
     */
    @RabbitListener(
            bindings = @QueueBinding(
                    value = @Queue(value = "${spring.rabbitmq.config.consumer.topic_team_success.queue}"),
                    exchange = @Exchange(value = "${spring.rabbitmq.config.consumer.topic_team_success.exchange}", type = ExchangeTypes.TOPIC),
                    key = "${spring.rabbitmq.config.consumer.topic_team_success.routing_key}"
            )
    )
    public void listener(String message) {

        log.info("接收消息:{}", message);
        try{
            NotifyRequestDTO requestDTO = JSON.parseObject(message, NotifyRequestDTO.class);
            log.info("拼团回调，组队完成且都已支付：{}", JSON.toJSONString(requestDTO));
            orderService.changeOrderMarketSettlement(requestDTO.getOutTradeNoList());

        }catch (Exception e){
            log.info("拼团回调异常：{}", e.getMessage());
        }
    }

}
