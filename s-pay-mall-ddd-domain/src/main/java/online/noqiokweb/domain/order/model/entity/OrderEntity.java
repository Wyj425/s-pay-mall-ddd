package online.noqiokweb.domain.order.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import online.noqiokweb.domain.order.model.valobj.OrderStatusVO;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author TheLastSavior noqiokweb.site @wyj
 * @description 聚合器聚合出的order实体,查询未支付的订单实体，其实就是订单实体
 * @create 7/8/2025 5:38 PM
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderEntity {
    private String productId;
    private String productName;
    private String orderId;
    private Date orderTime;
    private BigDecimal totalAmount;
    private OrderStatusVO orderStatusVO;
    private String payUrl;
}
