package online.noqiokweb.infrastructure.adapter.port;

import online.noqiokweb.domain.order.adapter.port.IProductPort;
import online.noqiokweb.domain.order.model.entity.ProductEntity;
import online.noqiokweb.infrastructure.gateway.ProductRPC;
import online.noqiokweb.infrastructure.gateway.dto.ProductDTO;
import org.springframework.stereotype.Component;

/**
 * @author TheLastSavior noqiokweb.site @wyj
 * @description
 * @create 7/8/2025 8:15 PM
 */
@Component
public class ProductPort implements IProductPort {
    private final ProductRPC productRPC;
    public ProductPort(ProductRPC productRPC) {
        this.productRPC = productRPC;
    }
    @Override
    public ProductEntity queryProductByProductId(String productId) {
        ProductDTO productDTO = productRPC.queryProductByProductId(productId);
        return ProductEntity.builder()
                .productId(productDTO.getProductId())
                .productName(productDTO.getProductName())
                .productDesc(productDTO.getProductDesc())
                .price(productDTO.getPrice())
                .build();
    }
}
