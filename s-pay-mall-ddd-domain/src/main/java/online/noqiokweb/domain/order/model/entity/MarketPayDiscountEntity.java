package online.noqiokweb.domain.order.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @author Fuzhengwei bugstack.cn @小傅哥
 * @description 营销支付优惠
 * @create 2025-02-06 16:44
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MarketPayDiscountEntity {

    /** 原始价格 */
    private BigDecimal originalPrice;
    /** 折扣金额 */
    private BigDecimal deductionPrice;
    /** 支付金额 */
    private BigDecimal payPrice;

}
