package online.noqiokweb.infrastructure.gateway.dto;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author TheLastSavior noqiokweb.site @wyj
 * @description
 * @create 7/3/2025 9:02 PM
 */
@Data
public class ProductDTO {
    private String productId;
    private String productName;
    private String productDesc;
    private BigDecimal price;
}
