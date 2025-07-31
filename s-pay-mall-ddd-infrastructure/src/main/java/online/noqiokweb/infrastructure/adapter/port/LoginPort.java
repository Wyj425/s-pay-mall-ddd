package online.noqiokweb.infrastructure.adapter.port;

import com.google.common.cache.Cache;
import online.noqiokweb.domain.auth.adapter.port.ILoginPort;

import online.noqiokweb.infrastructure.gateway.IWeixinApiService;
import online.noqiokweb.infrastructure.gateway.dto.WeixinQrCodeRequestDTO;
import online.noqiokweb.infrastructure.gateway.dto.WeixinQrCodeResponseDTO;
import online.noqiokweb.infrastructure.gateway.dto.WeixinTemplateMessageDTO;
import online.noqiokweb.infrastructure.gateway.dto.WeixinTokenResponseDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import retrofit2.Call;
import cn.hutool.core.util.IdUtil;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author TheLastSavior noqiokweb.site @wyj
 * @description
 * @create 7/7/2025 4:02 PM
 */
@Service
public class LoginPort implements ILoginPort {
    @Value("${weixin.config.app-id}")
    private String appid;
    @Value("${weixin.config.app-secret}")
    private String appSecret;
    @Value("${weixin.config.template_id}")
    private String template_id;
    @Resource
    private Cache<String, String> weixinAccessToken;
    @Resource
    private IWeixinApiService weixinApiService;
    @Override
    public String createQrCodeTicket() throws IOException {
        String sceneStr=IdUtil.getSnowflake().nextIdStr();
        return createQrCodeTicket(sceneStr);

    }

    @Override
    public String createQrCodeTicket(String sceneStr) throws IOException {
        //1.获取accessToken
        String accessToken = weixinAccessToken.getIfPresent(appid);
        if(accessToken == null){
            Call<WeixinTokenResponseDTO> call= weixinApiService.getToken("client_credential",appid,appSecret);
            WeixinTokenResponseDTO weixinTokenRes=call.execute().body();
            assert weixinTokenRes != null;
            accessToken=weixinTokenRes.getAccess_token();
            weixinAccessToken.put(appid,accessToken);
        }
        //2.生成ticket
        WeixinQrCodeRequestDTO weixinQrCodeReq=WeixinQrCodeRequestDTO.builder()
                .expire_seconds(2592000)
                .action_name(WeixinQrCodeRequestDTO.ActionNameTypeVO.QR_STR_SCENE.getCode()) //换成临时字符串
                .action_info(WeixinQrCodeRequestDTO.ActionInfo.builder()
                        .scene(WeixinQrCodeRequestDTO.ActionInfo.Scene.builder()
                                .scene_str(sceneStr)
                                .build())
                        .build())
                .build();

        Call<WeixinQrCodeResponseDTO> call=weixinApiService.createQrCode(accessToken,weixinQrCodeReq);
        WeixinQrCodeResponseDTO weixinQrCodeRes=call.execute().body();
        assert weixinQrCodeRes != null;

        return weixinQrCodeRes.getTicket();
    }

    @Override
    public void sendLoginTemplate(String openId) throws IOException {
        //1.获取accessToken
        String accessToken = weixinAccessToken.getIfPresent(appid);
        if(accessToken == null){
            Call<WeixinTokenResponseDTO> call=weixinApiService.getToken("client_credential",appid,appSecret);
            WeixinTokenResponseDTO weixinTokenRes=call.execute().body();
            assert weixinTokenRes != null;
            accessToken=weixinTokenRes.getAccess_token();
            weixinAccessToken.put(appid,accessToken);
        }

        // 2. 发送模板消息
        Map<String, Map<String, String>> data = new HashMap<>();
        WeixinTemplateMessageDTO.put(data, WeixinTemplateMessageDTO.TemplateKey.USER, openId);

        WeixinTemplateMessageDTO templateMessageDTO = new WeixinTemplateMessageDTO(openId, template_id);
        templateMessageDTO.setUrl("https://gaga.plus");
        templateMessageDTO.setData(data);

        Call<Void> call = weixinApiService.sendMessage(accessToken, templateMessageDTO);
        call.execute();
    }
}
