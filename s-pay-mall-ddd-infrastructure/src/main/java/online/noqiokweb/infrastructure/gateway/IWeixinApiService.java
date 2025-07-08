package online.noqiokweb.infrastructure.gateway;

import online.noqiokweb.infrastructure.gateway.dto.WeixinQrCodeRequestDTO;
import online.noqiokweb.infrastructure.gateway.dto.WeixinQrCodeResponseDTO;
import online.noqiokweb.infrastructure.gateway.dto.WeixinTemplateMessageDTO;
import online.noqiokweb.infrastructure.gateway.dto.WeixinTokenResponseDTO;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;


/**
 * 微信API服务
 */
public interface IWeixinApiService {
    /**
     * 服务器获取token
     * 文档：https://developers.weixin.qq.com/doc/service/api/base/api_getaccesstoken.html
     * @param grantType 填写 client_credential
     * @param appid 公众号的唯一凭证
     * @param appSecret 公众号的唯一凭证密钥
     * @return
     */
    @GET("cgi-bin/token")
    Call<WeixinTokenResponseDTO> getToken(@Query("grant_type") String grantType,
                                          @Query("appid") String appid,
                                          @Query("secret") String appSecret);

    /** 获取凭据ticket
     * 文档：https://developers.weixin.qq.com/doc/service/api/qrcode/qrcodes/api_createqrcode.html
     * @param accessToken
     * @param weixinQrCodeRequestDTO
     * @return
     */
    @POST("cgi-bin/qrcode/create")
    Call<WeixinQrCodeResponseDTO> createQrCode(@Query("access_token") String accessToken, @Body WeixinQrCodeRequestDTO weixinQrCodeRequestDTO);

    /**
     * 发送微信公众号模板消息
     * 文档：https://mp.weixin.qq.com/debug/cgi-bin/readtmpl?t=tmplmsg/faq_tmpl
     *
     * @param accessToken              getToken 获取的 token 信息
     * @param weixinTemplateMessageDTO 入参对象
     * @return 应答结果
     */
    @POST("cgi-bin/message/template/send")
    Call<Void> sendMessage(@Query("access_token") String accessToken, @Body WeixinTemplateMessageDTO weixinTemplateMessageDTO);
}
