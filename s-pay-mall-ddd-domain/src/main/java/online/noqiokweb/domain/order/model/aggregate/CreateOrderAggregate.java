package online.noqiokweb.domain.order.model.aggregate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Builder;
import online.noqiokweb.domain.order.model.entity.OrderEntity;
import online.noqiokweb.domain.order.model.entity.ProductEntity;
import online.noqiokweb.domain.order.model.valobj.OrderStatusVO;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.Date;

/**
 * @author TheLastSavior noqiokweb.site @wyj
 * @description
 * @create 7/8/2025 6:00 PM
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateOrderAggregate {
    private String userId;
    private ProductEntity productEntity;
    private OrderEntity orderEntity;
    public static OrderEntity buildOrderEntity(String productId,String productName,Integer marketType){
        return OrderEntity.builder()
                .productId(productId)
                .productName(productName)
                .orderId(RandomStringUtils.randomNumeric(12))
                .orderTime(new Date())
                .orderStatusVO(OrderStatusVO.CREATE)
                .marketType(marketType)
                .build();
    }
}
