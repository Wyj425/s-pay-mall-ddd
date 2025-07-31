package online.noqiokweb.domain.auth.service;

import com.google.common.cache.Cache;
import lombok.extern.slf4j.Slf4j;
import online.noqiokweb.domain.auth.adapter.port.ILoginPort;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;

/**
 * @author TheLastSavior noqiokweb.site @wyj
 * @description 
 * @create 7/7/2025 3:51 PM
 */
@Service
@Slf4j
public class WeixinLoginService implements ILoginService{
    @Resource
    private ILoginPort loginPort;
    @Resource
    private Cache<String, String> openidToken;
    @Override
    public String createQrCodeTicket() throws Exception {
        return loginPort.createQrCodeTicket();
    }

    @Override
    public String checkLogin(String ticket) {
        log.info("checkLogin ticket: [{}], length: {}, hashCode: {}", ticket, ticket.length(), ticket.hashCode());
        return openidToken.getIfPresent(ticket);
    }

    @Override
    public void saveLoginState(String ticket, String openId) throws IOException {
        log.info("saveLoginState ticket: [{}], length: {}, hashCode: {}", ticket, ticket.length(), ticket.hashCode());
        //保存登录信息
        openidToken.put(ticket, openId);
        //发送模版消息
        loginPort.sendLoginTemplate(openId);
    }
}
