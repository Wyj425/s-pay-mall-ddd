package online.noqiokweb.api.dto;

import lombok.Data;

import java.util.List;

/**
 * @author TheLastSavior noqiokweb.site @wyj
 * @description 拼团系统回调请求对象
 * @create 7/25/2025 7:21 下午
 */
@Data
public class NotifyRequestDTO {

    /** 组队ID */
    private String teamId;
    /** 外部单号 */
    private List<String> outTradeNoList;

}
