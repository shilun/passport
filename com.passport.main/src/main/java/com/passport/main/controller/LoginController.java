package com.passport.main.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.common.exception.BizException;
import com.common.util.RPCResult;
import com.common.util.StringUtils;
import com.common.web.IExecute;
import com.passport.rpc.AdminRPCService;
import com.passport.rpc.dto.UserDTO;
import com.passport.service.AdminUserInfoService;
import com.passport.main.AbstractClientController;
import com.passport.main.controller.dto.LoginDto;
import com.passport.main.controller.dto.PasswordChangeDto;
import com.passport.service.OperatorLogService;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.UUID;


@Controller
@RequestMapping(value = "/login", method = {RequestMethod.POST, RequestMethod.OPTIONS})
public class LoginController extends AbstractClientController {


    @Resource
    private AdminUserInfoService adminUserInfoService;
    @Resource
    private AdminRPCService adminRPCService;

    @Resource
    private OperatorLogService operatorLogService;

    @RequestMapping("in")
    @ResponseBody
    @ApiOperation(value = "密码登录")
    public Map<String, Object> login(@RequestBody LoginDto dto, HttpServletResponse response) {
        return buildMessage(new IExecute() {
            @Override
            public Object getData() {
                RPCResult<UserDTO> login = adminRPCService.login(dto.getAccount(), dto.getCode());
                if (!login.getSuccess()) {
                    throw new BizException("loginError", "登录失败，登录账户或密码错误");
                }
                putCookie("m_token", login.getData().getToken(), response);
                operatorLogService.logInfo("passport",login.getData().getPin(),"login",null);
                return login.getData();
            }
        });
    }


    @RequestMapping("out")
    @ResponseBody
    @ApiOperation(value = "登出")
    public Map<String, Object> loginOut() {
        return buildMessage(new IExecute() {
            @Override
            public Object getData() {
                return adminRPCService.loginOut(getToken());
            }
        });
    }

    @RequestMapping("check")
    @ResponseBody
    @ApiOperation(value = "检查token是否有效")
    public Map<String, Object> check() {
        return buildMessage(new IExecute() {
            @Override
            public Object getData() {
                String token = getToken();
                if (StringUtils.isBlank(token)) {
                    throw new BizException("token.error", "token error");
                }
                RPCResult<UserDTO> userDTOResult = adminRPCService.verfiyToken(token);
                return userDTOResult.getSuccess();
            }
        });
    }

    @RequestMapping("changePass")
    @ResponseBody
    @ApiOperation(value = "修改密码")
    public Map<String, Object> changePass(@RequestBody PasswordChangeDto dto) {
        return buildMessage(new IExecute() {
            @Override
            public Object getData() {
                String pin = getPin();
                operatorLogService.logInfo("passport",pin,"login",null);
                adminUserInfoService.changePass(pin, dto.getOldPassword(), dto.getNewPassword());
                return null;
            }
        });
    }
}
