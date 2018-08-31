package com.passport.web.controller;

import com.common.exception.BizException;
import com.common.util.RPCResult;
import com.common.util.StringUtils;
import com.common.util.model.YesOrNoEnum;
import com.common.web.IExecute;
import com.passport.rpc.AdminRPCService;
import com.passport.rpc.dto.UserDTO;
import com.passport.web.AbstractClientController;
import com.passport.web.controller.dto.LoginDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Map;


@Controller
@RequestMapping(value = "/login", method = {RequestMethod.POST, RequestMethod.OPTIONS})
public class LoginController extends AbstractClientController {

    private static final Logger LOGGER = Logger.getLogger(LoginController.class);

    @Resource
    private AdminRPCService adminRPCService;

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
                return login.getData();
            }
        });
    }


    @RequestMapping("out")
    @ResponseBody
    @ApiOperation(value = "登出")
    public RPCResult<Boolean> loginOut() {
        return buildRPCMessage(new IExecute() {
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
                RPCResult<UserDTO> userDTOResult = adminRPCService.verificationToken(token);
                return userDTOResult.getSuccess();
            }
        });
    }
}