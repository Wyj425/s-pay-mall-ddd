package online.noqiokweb.domain.order.adapter.repository;

import online.noqiokweb.domain.order.model.aggregate.CreateOrderAggregate;
import online.noqiokweb.domain.order.model.entity.OrderEntity;
import online.noqiokweb.domain.order.model.entity.PayOrderEntity;
import online.noqiokweb.domain.order.model.entity.ShopCartEntity;
import org.springframework.stereotype.Component;


public interface IOrderRepository {
    void doSaveOrder(CreateOrderAggregate orderAggregate);

    OrderEntity queryUnpayOrder(ShopCartEntity shopCartEntity);

    void updateOrderPayInfo(PayOrderEntity payOrder);
}
