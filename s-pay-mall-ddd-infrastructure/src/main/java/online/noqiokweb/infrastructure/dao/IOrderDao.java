package online.noqiokweb.infrastructure.dao;

import online.noqiokweb.infrastructure.dao.po.PayOrder;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;


import java.util.List;

@Mapper
public interface IOrderDao {
    void insert(PayOrder payOrder);

    PayOrder queryUnpayOrder(PayOrder payOrderReq);

    void updateOrderPayInfo(PayOrder payOrder);

//    void updateOrderPayInfo(PayOrder payOrder);
//
    void changeOrderPaySuccess(PayOrder order);

    List<String> queryNoPayNotifyOrder();

    List<String> queryTimeoutCloseOrderList();

    boolean changeOrderClose(String orderId);

    PayOrder queryOrderByOrderId(String orderId);

    void changeOrderMarketSettlement(@Param("outTradeNoList") List<String> outTradeNoList);

    void changeOrderDealDone(String orderId);

    List<PayOrder> queryUserOrderList(@Param("userId") String userId, @Param("lastId") Long lastId, @Param("pageSize") Integer pageSize);

    PayOrder queryOrderByUserIdAndOrderId(@Param("userId") String userId, @Param("orderId") String orderId);

    boolean refundOrder(@Param("userId") String userId, @Param("orderId") String orderId);

    boolean refundMarketOrder(String userId, String orderId);
}
