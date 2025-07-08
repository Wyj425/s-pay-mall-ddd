package online.noqiokweb.domain.order.adapter.port;

import online.noqiokweb.domain.order.model.entity.ProductEntity;
import org.springframework.stereotype.Component;


public interface IProductPort {
    ProductEntity queryProductByProductId(String productId);
}
