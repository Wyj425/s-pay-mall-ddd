package online.noqiokweb.domain.order.service;

import lombok.extern.slf4j.Slf4j;
import online.noqiokweb.domain.order.adapter.port.IProductPort;
import online.noqiokweb.domain.order.adapter.repository.IOrderRepository;
import online.noqiokweb.domain.order.model.aggregate.CreateOrderAggregate;
import online.noqiokweb.domain.order.model.entity.OrderEntity;
import online.noqiokweb.domain.order.model.entity.PayOrderEntity;
import online.noqiokweb.domain.order.model.entity.ProductEntity;
import online.noqiokweb.domain.order.model.entity.ShopCartEntity;
import online.noqiokweb.domain.order.model.valobj.OrderStatusVO;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;

/**
 * @author TheLastSavior noqiokweb.site @wyj
 * @description
 * @create 7/8/2025 5:27 PM
 */
@Slf4j
public abstract class AbstractOrderService implements IOrderService{
    //一个领域一般只有一个仓储和一个port，一个领域一个适配器
    protected final IOrderRepository repository;
    protected final IProductPort port;

    public AbstractOrderService(IOrderRepository repository, IProductPort port) {
        this.repository = repository;
        this.port = port;
    }

    @Override
    public PayOrderEntity createPayOrder(ShopCartEntity shopCartEntity) throws Exception {
        //1.查询当前用户是否存在掉单和未支付订单
        OrderEntity unpaidOrderEntity = repository.queryUnpayOrder(shopCartEntity);

        if(null!=unpaidOrderEntity&&unpaidOrderEntity.getOrderStatusVO().equals(OrderStatusVO.PAY_WAIT)){
            log.info("创建订单-存在，已存在未支付订单。userId:{} productId:{} orderId:{}",shopCartEntity.getUserId(),shopCartEntity.getProductId(),unpaidOrderEntity.getOrderId());
            return PayOrderEntity.builder()
                    .orderId(unpaidOrderEntity.getOrderId())
                    .payUrl(unpaidOrderEntity.getPayUrl())
                    .build();
        }else if(null!=unpaidOrderEntity&&unpaidOrderEntity.getOrderStatusVO().equals(OrderStatusVO.CREATE)){
            //todo 掉单
        }

        //2.查询商品&创建订单
        ProductEntity productEntity = port.queryProductByProductId(shopCartEntity.getProductId());
        OrderEntity orderEntity = CreateOrderAggregate.buildOrderEntity(productEntity.getProductId(),productEntity.getProductName());
        CreateOrderAggregate createOrderAggregate = CreateOrderAggregate.builder()
                .userId(shopCartEntity.getUserId())
                .productEntity(productEntity)
                .orderEntity(orderEntity)
                .build();
        this.doSaveOrder(createOrderAggregate);

        return PayOrderEntity.builder()
                .orderId(orderEntity.getOrderId())
                .payUrl("暂无")
                .build();
    }

    protected abstract void doSaveOrder(CreateOrderAggregate createOrderAggregate);
}
