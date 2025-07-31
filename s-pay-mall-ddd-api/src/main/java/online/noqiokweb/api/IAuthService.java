package online.noqiokweb.api;

import online.noqiokweb.api.response.Response;

public interface IAuthService {
    Response<String> weixinQrCodeTicket();

    Response<String> checkLogin(String ticket);


    Response<String> weixinQrCodeTicket(String sceneStr);

    Response<String> checkLogin(String ticket,String sceneStr);
}
