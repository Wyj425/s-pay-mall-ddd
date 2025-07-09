package online.noqiokweb.trigger.listener;

import com.google.common.eventbus.Subscribe;
import lombok.extern.slf4j.Slf4j;
import online.noqiokweb.domain.order.adapter.event.PaySuccessMessageEvent;
import org.springframework.stereotype.Component;

/**
 * @author TheLastSavior noqiokweb.site @wyj
 * @description 支付成功回调消息
 * @create 7/4/2025 10:51 PM
 */
@Slf4j
@Component
public class OrderPaySuccessListener {
    @Subscribe
    public void handleEvent(PaySuccessMessageEvent.PaySuccessMessage paySuccessMessage){
        log.info("支付成功消息 {},可以做接下来的事情，如：发货，充值，开会员，返利", paySuccessMessage);
    }
}
