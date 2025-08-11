package online.noqiokweb.trigger.listener;

import com.alibaba.fastjson.JSON;
import com.google.common.eventbus.Subscribe;
import lombok.extern.slf4j.Slf4j;
import online.noqiokweb.api.dto.NotifyRequestDTO;
import online.noqiokweb.domain.goods.service.IGoodsService;
import online.noqiokweb.domain.order.adapter.event.PaySuccessMessageEvent;
import online.noqiokweb.domain.order.service.OrderService;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
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

//    @Subscribe
//    public void handleEvent(String paySuccessMessageJson) {
//        log.info("收到支付成功消息 {}", paySuccessMessageJson);
//
//        PaySuccessMessageEvent.PaySuccessMessage paySuccessMessage = JSON.parseObject(paySuccessMessageJson, PaySuccessMessageEvent.PaySuccessMessage.class);
//
//        log.info("模拟发货（如；发货、充值、开户员、返利），单号:{}", paySuccessMessage.getTradeNo());
//
//        // 变更订单状态 - 发货完成&结算
//        goodsService.changeOrderDealDone(paySuccessMessage.getTradeNo());
//    }


    /**
     * 指定消费队列
     */
    @RabbitListener(
            bindings = @QueueBinding(
                    value = @Queue(value = "${spring.rabbitmq.config.consumer.topic_order_pay_success.queue}"),
                    exchange = @Exchange(value = "${spring.rabbitmq.config.consumer.topic_order_pay_success.exchange}", type = ExchangeTypes.TOPIC),
                    key = "${spring.rabbitmq.config.consumer.topic_order_pay_success.routing_key}"
            )
    )
    public void listener(String message) {

        log.info("收到支付成功消息:{}", message);
        try{
             PaySuccessMessageEvent.PaySuccessMessage paySuccessMessage= JSON.parseObject(message, PaySuccessMessageEvent.PaySuccessMessage.class);
            log.info("模拟发货（如；发货、充值、开户员、返利），单号:{}", JSON.toJSONString(paySuccessMessage.getTradeNo()));
            goodsService.changeOrderDealDone(paySuccessMessage.getTradeNo());

        }catch (Exception e){
            log.info("收到支付成功消息失败：{}", e.getMessage());
            throw e;
        }
    }

}
