package online.noqiokweb.infrastructure.adapter.port;

import lombok.extern.slf4j.Slf4j;
import online.noqiokweb.domain.order.adapter.port.IProductPort;
import online.noqiokweb.domain.order.model.entity.MarketPayDiscountEntity;
import online.noqiokweb.domain.order.model.entity.ProductEntity;
import online.noqiokweb.infrastructure.gateway.IGroupBuyMarketService;
import online.noqiokweb.infrastructure.gateway.ProductRPC;
import online.noqiokweb.infrastructure.gateway.dto.*;
import online.noqiokweb.infrastructure.gateway.response.Response;
import online.noqiokweb.types.exception.AppException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import retrofit2.Call;

import java.io.IOException;
import java.util.Date;

import static jdk.nashorn.internal.runtime.regexp.joni.Config.log;

/**
 * @author TheLastSavior noqiokweb.site @wyj
 * @description
 * @create 7/8/2025 8:15 PM
 */
@Slf4j
@Component
public class ProductPort implements IProductPort {
    @Value("${app.config.group-buy-market.source}")
    private String source;
    @Value("${app.config.group-buy-market.chanel}")
    private String chanel;
    @Value("${app.config.group-buy-market.notify-url}")
    private String notifyUrl;
    private final ProductRPC productRPC;
    private final IGroupBuyMarketService groupBuyMarketService;
    public ProductPort(ProductRPC productRPC, IGroupBuyMarketService groupBuyMarketService) {
        this.productRPC = productRPC;
        this.groupBuyMarketService = groupBuyMarketService;
    }
    @Override
    public ProductEntity queryProductByProductId(String productId) {
        ProductDTO productDTO = productRPC.queryProductByProductId(productId);
        return ProductEntity.builder()
                .productId(productDTO.getProductId())
                .productName(productDTO.getProductName())
                .productDesc(productDTO.getProductDesc())
                .price(productDTO.getPrice())
                .build();
    }

    @Override
    public MarketPayDiscountEntity lockMarketPayOrder(String userId, String teamId, Long activityId, String productId, String orderId) {
        LockMarketPayOrderRequestDTO requestDTO = LockMarketPayOrderRequestDTO.builder()
                .userId(userId)
                .teamId(teamId)
                .activityId(activityId)
                .goodsId(productId)
                .source(source)
                .channel(chanel)
                .outTradeNo(orderId)
                .notifyUrl(notifyUrl)
                .build();
        try{
            Call<Response<LockMarketPayOrderResponseDTO>> call= groupBuyMarketService.lockMarketPayOrder(requestDTO);
            Response<LockMarketPayOrderResponseDTO> response = call.execute().body();
            if(response== null) return null;

            if(!"0000".equals(response.getCode())){
                throw new AppException(response.getCode(),response.getInfo());
            }

            LockMarketPayOrderResponseDTO responseDTO= response.getData();

            return MarketPayDiscountEntity.builder()
                    .originalPrice(responseDTO.getOriginalPrice())
                    .deductionPrice(responseDTO.getDeductionPrice())
                    .payPrice(responseDTO.getPayPrice())
                    .build();
        } catch (IOException e) {
            return null;
        }

    }

    @Override
    public void settlementMarketPayOrder(String userId, String orderId, Date orderTime) {
        SettlementMarketPayOrderRequestDTO requestDTO=new SettlementMarketPayOrderRequestDTO();
        requestDTO.setSource( source);
        requestDTO.setChannel( chanel);
        requestDTO.setUserId( userId);
        requestDTO.setOutTradeNo( orderId);
        requestDTO.setOutTradeTime( orderTime);

        try{
            Call<Response<SettlementMarketPayOrderResponseDTO>> call= groupBuyMarketService.settlementMarketPayOrder(requestDTO);
            Response<SettlementMarketPayOrderResponseDTO> response = call.execute().body();
            if(response== null) return;

            if(!"0000".equals(response.getCode())){
                throw new AppException(response.getCode(),response.getInfo());
            }
        } catch (IOException e) {
            log.info("调用拼团结算订单失败 orderId:{}",orderId,e);
        }
    }
}
