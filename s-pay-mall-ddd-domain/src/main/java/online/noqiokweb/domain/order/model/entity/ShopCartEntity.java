package online.noqiokweb.domain.order.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author TheLastSavior noqiokweb.site @wyj
 * @description 购物车实体对象。
 * @create 7/8/2025 5:22 PM
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ShopCartEntity {
    private String userId;
    private String productId;
}
