package online.noqiokweb.domain.order.adapter.repository;

import online.noqiokweb.domain.order.model.aggregate.CreateOrderAggregate;
import online.noqiokweb.domain.order.model.entity.OrderEntity;
import online.noqiokweb.domain.order.model.entity.PayOrderEntity;
import online.noqiokweb.domain.order.model.entity.ShopCartEntity;
import org.springframework.stereotype.Component;

import java.util.List;


public interface IOrderRepository {
    void doSaveOrder(CreateOrderAggregate orderAggregate);

    OrderEntity queryUnpayOrder(ShopCartEntity shopCartEntity);

    void updateOrderPayInfo(PayOrderEntity payOrder);

    void changeOrderPaySuccess(String orderId);

    List<String> queryNoPayNotifyOrder();

    List<String> queryTimeoutCloseOrderList();

    boolean changeOrderClose(String orderId);
}
