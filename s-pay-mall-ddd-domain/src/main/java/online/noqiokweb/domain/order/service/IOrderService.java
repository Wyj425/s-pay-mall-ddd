package online.noqiokweb.domain.order.service;

import com.alipay.api.AlipayApiException;
import online.noqiokweb.domain.order.model.entity.OrderEntity;
import online.noqiokweb.domain.order.model.entity.PayOrderEntity;
import online.noqiokweb.domain.order.model.entity.ShopCartEntity;

import java.util.Date;
import java.util.List;

public interface IOrderService {

    PayOrderEntity createPayOrder(ShopCartEntity shopCartEntity) throws Exception;

    void changeOrderPaySuccess(String orderId,Date orderTime);

    List<String> queryNoPayNotifyOrder();

    List<String> queryTimeoutCloseOrderList();

    boolean changeOrderClose(String orderId);

    void changeOrderMarketSettlement(List<String> outTradeNoList);


    List<OrderEntity> queryUserOrderList(String userId, Long lastId, Integer pageSize);

    boolean refundOrder(String userId, String orderId);
    boolean refundMarketOrder(String userId, String orderId);
    /**
     * 接收拼团退单消息
     */
    boolean refundPayOrder(String userId, String orderId) throws AlipayApiException;

}
