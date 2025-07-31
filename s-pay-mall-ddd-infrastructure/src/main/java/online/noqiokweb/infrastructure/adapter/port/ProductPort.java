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
        log.info("进入了调用拼团接口流程");
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
        try {
            Call<Response<LockMarketPayOrderResponseDTO>> call = groupBuyMarketService.lockMarketPayOrder(requestDTO);
            // .execute() 会抛出 IOException，所以把它放在 try 块里
            Response<LockMarketPayOrderResponseDTO> response = call.execute().body();

            if (response == null) {
                // response 为 null 可能是网络层问题或者反序列化失败，可以视为一种严重错误
                throw new AppException("REMOTE_SERVICE_ERROR", "调用拼团服务返回为空");
            }

            if (!"0000".equals(response.getCode())) {
                log.warn("调用拼团服务业务失败，Code: {}, Info: {}", response.getCode(), response.getInfo());
                throw new AppException(response.getCode(), response.getInfo());
            }

            LockMarketPayOrderResponseDTO responseDTO = response.getData();

            // 可选：检查 responseDTO 或其内部字段是否为 null
            if (responseDTO == null) {
                throw new AppException("REMOTE_DATA_ERROR", "调用拼团服务成功但返回数据体为空");
            }

            return MarketPayDiscountEntity.builder()
                    .originalPrice(responseDTO.getOriginalPrice())
                    .deductionPrice(responseDTO.getDeductionPrice())
                    .payPrice(responseDTO.getPayPrice())
                    .build();

        } catch (IOException e) {
            // 捕获IO异常，记录详细日志，并包装成自定义异常抛出
            log.error("调用拼团商城接口发生网络异常！请求: {}", requestDTO, e);
            // 将原始异常 e 作为 cause 传入，不丢失任何信息
            throw new AppException("NETWORK_IO_ERROR", "调用拼团服务网络异常", e);
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
