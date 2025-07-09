package online.noqiokweb.config;

import com.alipay.api.AlipayClient;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author TheLastSavior noqiokweb.site @wyj
 * @description @EnableConfigurationProperties(AliPayConfigProperties.class)
 * 表示启用上面那个配置属性类，使其生效并注入。
 * @Bean("alipayClient")
 * 把通过支付宝参数构建的 AlipayClient 实例注册为 Spring Bean，方便其他服务注入使用
 * @create 7/3/2025 9:44 PM
 */
@Configuration
@EnableConfigurationProperties(AliPayConfigProperties.class)
public class AliPayConfig {
    @Bean("alipayClient")
    public AlipayClient alipayClient(AliPayConfigProperties aliPayConfigProperties){
        return new com.alipay.api.DefaultAlipayClient(aliPayConfigProperties.getGatewayUrl(),
                aliPayConfigProperties.getApp_id(),
                aliPayConfigProperties.getMerchant_private_key(),
                aliPayConfigProperties.getFormat(),
                aliPayConfigProperties.getCharset(),
                aliPayConfigProperties.getAlipay_public_key(),
                aliPayConfigProperties.getSign_type());
    }
}
