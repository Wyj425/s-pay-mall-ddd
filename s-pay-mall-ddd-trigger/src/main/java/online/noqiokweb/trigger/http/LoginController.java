package online.noqiokweb.trigger.http;

import lombok.extern.slf4j.Slf4j;
import online.noqiokweb.api.IAuthService;
import online.noqiokweb.api.response.Response;
import online.noqiokweb.domain.auth.service.ILoginService;
import online.noqiokweb.types.common.Constants;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author TheLastSavior noqiokweb.site @wyj
 * @description
 * @create 7/7/2025 4:43 PM
 */
@Slf4j
@RestController()
@CrossOrigin("*")
@RequestMapping("/api/v1/login/")
public class LoginController implements IAuthService {
    @Resource
    private ILoginService loginService;
    @Override
    @RequestMapping(value="weixin_qrcode_ticket",method = RequestMethod.GET)
    public Response<String> weixinQrCodeTicket() {
        try{
            String qrCodeTicket=loginService.createQrCodeTicket();
            log.info("生成微信扫码登录,ticket:{}",qrCodeTicket);
            return Response.<String>builder()
                    .code(Constants.ResponseCode.SUCCESS.getCode())
                    .info(Constants.ResponseCode.SUCCESS.getInfo())
                    .data(qrCodeTicket)
                    .build();
        } catch (Exception e) {
            log.info("生成微信扫码登录失败",e);
            return Response.<String>builder()
                    .code(Constants.ResponseCode.UN_ERROR.getCode())
                    .info(Constants.ResponseCode.UN_ERROR.getInfo())
                    .build();
        }
    }

    @Override
    @RequestMapping(value="check_login",method = RequestMethod.GET)
    public Response<String> checkLogin(String ticket) {
        try{
            String openid= loginService.checkLogin(ticket);
            log.info("扫码检测登录结果,ticket:{},openid:{}",ticket,openid);
            if(StringUtils.isNotBlank(openid)){
                return Response.<String>builder()
                        .code(Constants.ResponseCode.SUCCESS.getCode())
                        .info(Constants.ResponseCode.SUCCESS.getInfo())
                        .data(openid)
                        .build();
            }else{
                return Response.<String>builder()
                        .code(Constants.ResponseCode.NO_LOGIN.getCode())
                        .info(Constants.ResponseCode.NO_LOGIN.getInfo())
                        .build();
            }
        } catch (Exception e) {
            log.info("扫码检测登录结果失败 ticket:{}",ticket,e);
            return Response.<String>builder()
                    .code(Constants.ResponseCode.UN_ERROR.getCode())
                    .info(Constants.ResponseCode.UN_ERROR.getInfo())
                    .build();
        }
    }

    @Override
    @GetMapping("weixin_qrcode_ticket_scene")
    public Response<String> weixinQrCodeTicket(@RequestParam String sceneStr) {
        try{
            String qrCodeTicket=loginService.createQrCodeTicket(sceneStr);
            log.info("生成微信扫码登录,ticket:{}",qrCodeTicket);
            return Response.<String>builder()
                    .code(Constants.ResponseCode.SUCCESS.getCode())
                    .info(Constants.ResponseCode.SUCCESS.getInfo())
                    .data(qrCodeTicket)
                    .build();
        } catch (Exception e) {
            log.info("生成微信扫码登录失败",e);
            return Response.<String>builder()
                    .code(Constants.ResponseCode.UN_ERROR.getCode())
                    .info(Constants.ResponseCode.UN_ERROR.getInfo())
                    .build();
        }

    }

    @Override
    @GetMapping("check_login_scene")
    public Response<String> checkLogin(@RequestParam String ticket, @RequestParam String sceneStr) {
        try{
            String openid= loginService.checkLogin(ticket,sceneStr);
            log.info("扫码检测登录结果,ticket:{},openid:{},sceneStr:{}",ticket,openid,sceneStr);
            if(StringUtils.isNotBlank(openid)){
                return Response.<String>builder()
                        .code(Constants.ResponseCode.SUCCESS.getCode())
                        .info(Constants.ResponseCode.SUCCESS.getInfo())
                        .data(openid)
                        .build();
            }else{
                return Response.<String>builder()
                        .code(Constants.ResponseCode.NO_LOGIN.getCode())
                        .info(Constants.ResponseCode.NO_LOGIN.getInfo())
                        .build();
            }
        } catch (Exception e) {
            log.info("扫码检测登录结果失败 ticket:{}",ticket,e);
            return Response.<String>builder()
                    .code(Constants.ResponseCode.UN_ERROR.getCode())
                    .info(Constants.ResponseCode.UN_ERROR.getInfo())
                    .build();
        }
    }
}
