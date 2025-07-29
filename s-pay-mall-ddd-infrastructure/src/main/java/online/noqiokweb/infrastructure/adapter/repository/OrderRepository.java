package online.noqiokweb.infrastructure.adapter.repository;

import com.alibaba.fastjson.JSON;
import com.google.common.eventbus.EventBus;
import online.noqiokweb.domain.order.adapter.repository.IOrderRepository;
import online.noqiokweb.domain.order.adapter.event.PaySuccessMessageEvent;
import online.noqiokweb.domain.order.model.aggregate.CreateOrderAggregate;
import online.noqiokweb.domain.order.model.entity.OrderEntity;
import online.noqiokweb.domain.order.model.entity.PayOrderEntity;
import online.noqiokweb.domain.order.model.entity.ProductEntity;
import online.noqiokweb.domain.order.model.entity.ShopCartEntity;
import online.noqiokweb.domain.order.model.valobj.MarketTypeVO;
import online.noqiokweb.domain.order.model.valobj.OrderStatusVO;
import online.noqiokweb.infrastructure.dao.IOrderDao;
import online.noqiokweb.infrastructure.dao.po.PayOrder;
import online.noqiokweb.types.event.BaseEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author TheLastSavior noqiokweb.site @wyj
 * @description
 * @create 7/8/2025 8:18 PM
 */
@Repository
public class OrderRepository implements IOrderRepository {
    @Resource
    private PaySuccessMessageEvent paySuccessMessageEvent;
    @Resource
    private IOrderDao orderDao;
    @Resource
    private EventBus eventBUs;
    @Autowired
    private EventBus eventBus;

    @Override
    public void doSaveOrder(CreateOrderAggregate orderAggregate) {
        String userId = orderAggregate.getUserId();
        ProductEntity productEntity = orderAggregate.getProductEntity();
        OrderEntity orderEntity = orderAggregate.getOrderEntity();

        PayOrder order = new PayOrder();
        order.setUserId(userId);
        order.setProductId(productEntity.getProductId());
        order.setProductName(productEntity.getProductName());
        order.setOrderId(orderEntity.getOrderId());
        order.setOrderTime(orderEntity.getOrderTime());
        order.setTotalAmount(productEntity.getPrice());
        order.setStatus(orderEntity.getOrderStatusVO().getCode());
        order.setMarketType(MarketTypeVO.NO_MARKET.getCode());
        order.setMarketDeductionAmount(BigDecimal.ZERO);
        order.setPayAmount(productEntity.getPrice());
        order.setMarketType(orderEntity.getMarketType());

        orderDao.insert(order);
    }
    @Override
    public OrderEntity queryUnpayOrder(ShopCartEntity shopCartEntity) {
        // 1. 封装参数
        PayOrder orderReq = new PayOrder();
        orderReq.setUserId(shopCartEntity.getUserId());
        orderReq.setProductId(shopCartEntity.getProductId());

        // 2. 查询到订单
        PayOrder order = orderDao.queryUnpayOrder(orderReq);
        if (null == order) return null;

        // 3. 返回结果
        return OrderEntity.builder()
                .productId(order.getProductId())
                .productName(order.getProductName())
                .orderId(order.getOrderId())
                .orderStatusVO(OrderStatusVO.valueOf(order.getStatus()))
                .orderTime(order.getOrderTime())
                .totalAmount(order.getTotalAmount())
                .payUrl(order.getPayUrl())
                .marketType(order.getMarketType())
                .marketDeductionAmount(order.getMarketDeductionAmount())
                .payAmount(order.getPayAmount())
                .build();
    }

    @Override
    public void updateOrderPayInfo(PayOrderEntity payOrder) {
        //就算这里没更新成功，我们后续有任务补偿，即通过job不断去扫描
        PayOrder payOrderReq=PayOrder.builder()
                .userId(payOrder.getUserId())
                .orderId(payOrder.getOrderId())
                .payUrl(payOrder.getPayUrl())
                .status(payOrder.getOrderStatus().getCode())
                .marketType(payOrder.getMarketType())
                .marketDeductionAmount(payOrder.getMarketDeductionAmount())
                .payAmount(payOrder.getPayAmount())
                .build();
        orderDao.updateOrderPayInfo(payOrderReq);
    }

    @Override
    public void changeOrderPaySuccess(String orderId, Date orderTime) {
        PayOrder payOrderReq=new PayOrder();
        payOrderReq.setOrderId(orderId);
        payOrderReq.setStatus(OrderStatusVO.PAY_SUCCESS.getCode());
        orderDao.changeOrderPaySuccess(payOrderReq);
        BaseEvent.EventMessage<PaySuccessMessageEvent.PaySuccessMessage> paySuccessMessageEventMessage= paySuccessMessageEvent.buildEventMessage(PaySuccessMessageEvent.PaySuccessMessage.builder()
                        .tradeNo(orderId)
                .build());
        PaySuccessMessageEvent.PaySuccessMessage paySuccessMessage=paySuccessMessageEventMessage.getData();
        eventBus.post(JSON.toJSONString(paySuccessMessage));
    }

    @Override
    public List<String> queryNoPayNotifyOrder() {
        return orderDao.queryNoPayNotifyOrder();
    }

    @Override
    public List<String> queryTimeoutCloseOrderList() {
        return orderDao.queryTimeoutCloseOrderList();
    }

    @Override
    public boolean changeOrderClose(String orderId) {
        return orderDao.changeOrderClose(orderId);
    }

    @Override
    public OrderEntity queryOrderByOrderId(String orderId) {
        PayOrder payOrder=orderDao.queryOrderByOrderId(orderId);
        if(null==payOrder) return null;
        return OrderEntity.builder()
                .userId(payOrder.getUserId())
                .productId(payOrder.getProductId())
                .productName(payOrder.getProductName())
                .orderId(payOrder.getOrderId())
                .orderStatusVO(OrderStatusVO.valueOf(payOrder.getStatus()))
                .orderTime(payOrder.getOrderTime())
                .totalAmount(payOrder.getTotalAmount())
                .payUrl(payOrder.getPayUrl())
                .marketType(payOrder.getMarketType())
                .marketDeductionAmount(payOrder.getMarketDeductionAmount())
                .payAmount(payOrder.getPayAmount())
                .build();

    }

    @Override
    public void changeMarketOrderPaySuccess(String orderId) {
        PayOrder payOrderReq=new PayOrder();
        payOrderReq.setOrderId(orderId);
        payOrderReq.setStatus(OrderStatusVO.PAY_SUCCESS.getCode());
        orderDao.changeOrderPaySuccess(payOrderReq);
    }

    @Override
    public void changeOrderMarketSettlement(List<String> outTradeNoList) {
        orderDao.changeOrderMarketSettlement(outTradeNoList);
        outTradeNoList.forEach(outTradeNo->{
            BaseEvent.EventMessage<PaySuccessMessageEvent.PaySuccessMessage> paySuccessMessageEventMessage= paySuccessMessageEvent.buildEventMessage(PaySuccessMessageEvent.PaySuccessMessage.builder()
                    .tradeNo(outTradeNo)
                    .build());
            PaySuccessMessageEvent.PaySuccessMessage paySuccessMessage=paySuccessMessageEventMessage.getData();
            eventBus.post(JSON.toJSONString(paySuccessMessage));
        });
    }
}
