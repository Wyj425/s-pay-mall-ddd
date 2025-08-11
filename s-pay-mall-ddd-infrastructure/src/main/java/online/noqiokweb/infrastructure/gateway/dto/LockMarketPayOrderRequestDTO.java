package online.noqiokweb.infrastructure.gateway.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Fuzhengwei bugstack.cn @小傅哥
 * @description 营销支付锁单请求对象
 * @create 2025-01-11 13:55
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LockMarketPayOrderRequestDTO {

    // 用户ID
    private String userId;
    // 拼单组队ID - 可为空，为空则创建新组队ID
    private String teamId;
    // 活动ID
    private Long activityId;
    // 商品ID
    private String goodsId;
    // 渠道
    private String source;
    // 来源
    private String channel;
    // 外部交易单号
    private String outTradeNo;

    //回调信息
    private NotifyConfigVO notifyConfigVO;

    public void setNotifyUrl(String notifyUrl){
        NotifyConfigVO notifyConfigVO = new NotifyConfigVO();
        notifyConfigVO.setNotifyUrl(notifyUrl);
        notifyConfigVO.setNotifyType("HTTP");
        this.setNotifyConfigVO(notifyConfigVO);
    }
    public void setNotifyMQ(){
        NotifyConfigVO notifyConfigVO = new NotifyConfigVO();;
        notifyConfigVO.setNotifyType("MQ");
        this.setNotifyConfigVO(notifyConfigVO);
    }

    @Data
    public static class NotifyConfigVO{
        // 回调方式
        private String notifyType;
        // 回调地址
        private String notifyUrl;
        // 回调MQ
        private String notifyMQ;
    }

}
