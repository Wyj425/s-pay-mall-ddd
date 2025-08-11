package online.noqiokweb.domain.order.adapter.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import online.noqiokweb.types.event.BaseEvent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author TheLastSavior noqiokweb.site @wyj
 * @description 支付成功消息事件
 * @create 7/9/2025 4:58 PM
 */
@Component
public class PaySuccessMessageEvent extends BaseEvent<PaySuccessMessageEvent.PaySuccessMessage> {
    @Value("${spring.rabbitmq.config.producer.topic_order_pay_success.routing_key}")
    private String TOPIC_ORDER_PAY_SUCCESS;
    @Override
    public EventMessage<PaySuccessMessage> buildEventMessage(PaySuccessMessage data) {
        return EventMessage.<PaySuccessMessage>builder()
                .id("")
                .timestamp(new Date())
                .data(data)
                .build();
    }

    @Override
    public String topic() {
        return TOPIC_ORDER_PAY_SUCCESS;
    }
    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PaySuccessMessage {
        private String orderId;
        private String tradeNo;
    }
}
