package online.noqiokweb.infrastructure.dao;

import online.noqiokweb.infrastructure.dao.po.PayOrder;
import org.apache.ibatis.annotations.Mapper;


import java.util.List;

@Mapper
public interface IOrderDao {
    void insert(PayOrder payOrder);

    PayOrder queryUnpayOrder(PayOrder payOrderReq);

//    void updateOrderPayInfo(PayOrder payOrder);
//
//    void changeOrderPaySuccess(PayOrder order);
//
//    List<String> queryNoPayNotifyOrder();
//
//    List<String> queryTimeoutCloseOrderList();
//
//    boolean changeOrderClose(String orderId);
}
