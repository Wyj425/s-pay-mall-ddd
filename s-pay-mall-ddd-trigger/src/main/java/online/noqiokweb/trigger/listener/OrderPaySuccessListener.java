package online.noqiokweb.trigger.listener;

import com.alibaba.fastjson.JSON;
import com.google.common.eventbus.Subscribe;
import lombok.extern.slf4j.Slf4j;
import online.noqiokweb.domain.goods.service.IGoodsService;
import online.noqiokweb.domain.order.adapter.event.PaySuccessMessageEvent;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author TheLastSavior noqiokweb.site @wyj
 * @description 支付成功回调消息
 * @create 7/4/2025 10:51 PM
 */
@Slf4j
@Component
public class OrderPaySuccessListener {

    @Resource
    private IGoodsService goodsService;

    @Subscribe
    public void handleEvent(String paySuccessMessageJson) {
        log.info("收到支付成功消息 {}", paySuccessMessageJson);

        PaySuccessMessageEvent.PaySuccessMessage paySuccessMessage = JSON.parseObject(paySuccessMessageJson, PaySuccessMessageEvent.PaySuccessMessage.class);

        log.info("模拟发货（如；发货、充值、开户员、返利），单号:{}", paySuccessMessage.getTradeNo());

        // 变更订单状态 - 发货完成&结算
        goodsService.changeOrderDealDone(paySuccessMessage.getTradeNo());
    }

}
