package online.noqiokweb.infrastructure.gateway;

import online.noqiokweb.infrastructure.gateway.dto.LockMarketPayOrderRequestDTO;
import online.noqiokweb.infrastructure.gateway.dto.LockMarketPayOrderResponseDTO;
import online.noqiokweb.infrastructure.gateway.dto.SettlementMarketPayOrderRequestDTO;
import online.noqiokweb.infrastructure.gateway.dto.SettlementMarketPayOrderResponseDTO;
import online.noqiokweb.infrastructure.gateway.response.Response;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface IGroupBuyMarketService {
    @POST("api/v1/gbm/trade/lock_market_pay_order")
    Call<Response<LockMarketPayOrderResponseDTO>> lockMarketPayOrder(@Body LockMarketPayOrderRequestDTO requestDTO);
    @POST("api/v1/gbm/trade/settlement_market_pay_order")
    Call<Response<SettlementMarketPayOrderResponseDTO>> settlementMarketPayOrder(@Body SettlementMarketPayOrderRequestDTO requestDTO);
}
