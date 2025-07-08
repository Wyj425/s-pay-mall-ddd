package online.noqiokweb.infrastructure.gateway;

import online.noqiokweb.infrastructure.gateway.dto.ProductDTO;
import org.springframework.stereotype.Service;


import java.math.BigDecimal;

/**
 * @author TheLastSavior noqiokweb.site @wyj
 * @description
 * @create 7/3/2025 9:00 PM
 */
@Service
public class ProductRPC {
    public ProductDTO queryProductByProductId(String productId){
        ProductDTO productVO = new ProductDTO();
        productVO.setProductId(productId);
        productVO.setProductName("测试商品");
        productVO.setProductDesc("这是一个测试商品");
        productVO.setPrice(new BigDecimal("1.68"));
        return productVO;
    }
}
