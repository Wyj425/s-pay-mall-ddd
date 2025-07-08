package online.noqiokweb.domain.order.service;

import online.noqiokweb.domain.order.model.entity.PayOrderEntity;
import online.noqiokweb.domain.order.model.entity.ShopCartEntity;

public interface IOrderService {
    PayOrderEntity createPayOrder(ShopCartEntity shopCartEntity) throws Exception;
}
