package online.noqiokweb.infrastructure.dao.po;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author TheLastSavior noqiokweb.site @wyj
 * @description 订单信息
 * @create 7/3/2025 8:06 PM
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PayOrder {
    private Long id;
    private String userId;
    private String productId;
    private String productName;
    private String orderId;
    private Date orderTime;
    private BigDecimal totalAmount;
    private String status;
    private String payUrl;
    private Date payTime;
    private Date createTime;
    private Date updateTime;
}
