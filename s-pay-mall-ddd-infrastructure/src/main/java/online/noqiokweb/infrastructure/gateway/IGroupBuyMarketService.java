package online.noqiokweb.infrastructure.gateway;

import online.noqiokweb.infrastructure.gateway.dto.*;
import online.noqiokweb.infrastructure.gateway.response.Response;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface IGroupBuyMarketService {
    @POST("api/v1/gbm/trade/lock_market_pay_order")
    Call<Response<LockMarketPayOrderResponseDTO>> lockMarketPayOrder(@Body LockMarketPayOrderRequestDTO requestDTO);
    @POST("api/v1/gbm/trade/settlement_market_pay_order")
    Call<Response<SettlementMarketPayOrderResponseDTO>> settlementMarketPayOrder(@Body SettlementMarketPayOrderRequestDTO requestDTO);
    /**
     * 营销拼团退单
     *
     * @param requestDTO 退单请求信息
     * @return 退单结果信息
     */
    @POST("api/v1/gbm/trade/refund_market_pay_order")
    Call<Response<RefundMarketPayOrderResponseDTO>> refundMarketPayOrder(@Body RefundMarketPayOrderRequestDTO requestDTO);
}
