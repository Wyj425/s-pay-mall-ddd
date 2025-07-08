package online.noqiokweb.domain.order.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import online.noqiokweb.domain.order.model.valobj.OrderStatusVO;

/**
 * @author TheLastSavior noqiokweb.site @wyj
 * @description 返回给前端的实体对象
 * @create 7/8/2025 5:25 PM
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PayOrderEntity {
    private String userId;
    private String orderId;
    private String payUrl;
    private OrderStatusVO orderStatus;
}
