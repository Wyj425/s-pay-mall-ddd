package online.noqiokweb.domain.order.adapter.port;

import online.noqiokweb.domain.order.model.entity.MarketPayDiscountEntity;
import online.noqiokweb.domain.order.model.entity.ProductEntity;
import org.springframework.stereotype.Component;


public interface IProductPort {
    ProductEntity queryProductByProductId(String productId);
    MarketPayDiscountEntity lockMarketPayOrder(String userId, String teamId, Long activityId, String productId, String orderId);
}
