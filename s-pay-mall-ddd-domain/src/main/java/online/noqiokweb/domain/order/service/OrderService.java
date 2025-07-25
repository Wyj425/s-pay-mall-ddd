package online.noqiokweb.domain.order.service;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson2.JSON;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.request.AlipayTradePagePayRequest;
import lombok.extern.slf4j.Slf4j;
import online.noqiokweb.domain.order.adapter.port.IProductPort;
import online.noqiokweb.domain.order.adapter.repository.IOrderRepository;
import online.noqiokweb.domain.order.model.aggregate.CreateOrderAggregate;
import online.noqiokweb.domain.order.model.entity.MarketPayDiscountEntity;
import online.noqiokweb.domain.order.model.entity.PayOrderEntity;
import online.noqiokweb.domain.order.model.valobj.MarketTypeVO;
import online.noqiokweb.domain.order.model.valobj.OrderStatusVO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.websocket.server.ServerEndpoint;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author TheLastSavior noqiokweb.site @wyj
 * @description
 * @create 7/8/2025 7:51 PM
 */
@Slf4j
@Service
public class OrderService extends AbstractOrderService{
    @Value("${alipay.notify_url}")
    private String notifyUrl;
    @Value("${alipay.return_url}")
    private String returnUrl;
    @Resource
    private AlipayClient alipayClient;// todo 放到基础层
    public OrderService(IOrderRepository  repository, IProductPort port){
        super(repository, port);
    }

    @Override
    protected MarketPayDiscountEntity lockMarketPayOrder(String userId, String teamId, Long activityId, String productId, String orderId) {
        return port.lockMarketPayOrder(userId, teamId, activityId, productId, orderId);
    }

    @Override
    //更新数据库,状态为CREATE
    protected void doSaveOrder(CreateOrderAggregate orderAggregate) {
        repository.doSaveOrder(orderAggregate);
    }

    @Override
    protected PayOrderEntity doPrePayOrder(String userId, String productId, String productName, String orderId, BigDecimal totalAmount, MarketPayDiscountEntity marketPayDiscountEntity) throws AlipayApiException {
        BigDecimal payAmount=null==marketPayDiscountEntity?totalAmount:marketPayDiscountEntity.getPayPrice();
        AlipayTradePagePayRequest request = new AlipayTradePagePayRequest();
        request.setNotifyUrl(notifyUrl);
        request.setReturnUrl(returnUrl);

        JSONObject bizContent = new JSONObject();
        bizContent.put("out_trade_no", orderId);
        bizContent.put("total_amount", payAmount.toString());
        bizContent.put("subject", productName);
        bizContent.put("product_code", "FAST_INSTANT_TRADE_PAY");
        request.setBizContent(bizContent.toString());

        String form = alipayClient.pageExecute(request).getBody();

        PayOrderEntity payOrderEntity = new PayOrderEntity();
        payOrderEntity.setOrderId(orderId);
        payOrderEntity.setPayUrl(form);
        payOrderEntity.setOrderStatus(OrderStatusVO.PAY_WAIT);

        // 营销信息
        payOrderEntity.setMarketType(null == marketPayDiscountEntity ? MarketTypeVO.NO_MARKET.getCode() : MarketTypeVO.GROUP_BUY_MARKET.getCode());
        payOrderEntity.setMarketDeductionAmount(null == marketPayDiscountEntity ? BigDecimal.ZERO : marketPayDiscountEntity.getDeductionPrice());
        payOrderEntity.setPayAmount(payAmount);

        //更新数据库,状态为PAY_WAIT
        repository.updateOrderPayInfo(payOrderEntity);
        return payOrderEntity;
    }

    @Override
    protected PayOrderEntity doPrePayOrder(String userId, String productId, String productName, String orderId, BigDecimal totalAmount) throws AlipayApiException {
        return doPrePayOrder(userId, productId, productName, orderId, totalAmount, null);
    }
    @Override
    public void changeOrderPaySuccess(String orderId) {

        repository.changeOrderPaySuccess(orderId);

        //mq一般发送json格式
        //eventBus.post(JSON.toJSONString(payOrderReq));
    }

    @Override
    public List<String> queryNoPayNotifyOrder() {
        return repository.queryNoPayNotifyOrder();
    }

    @Override
    public List<String> queryTimeoutCloseOrderList() {
        return repository.queryTimeoutCloseOrderList();
    }

    @Override
    public boolean changeOrderClose(String orderId) {
        return repository.changeOrderClose(orderId);
    }
}
