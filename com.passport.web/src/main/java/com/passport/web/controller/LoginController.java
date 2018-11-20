package com.passport.web.controller;

import com.common.exception.BizException;
import com.common.util.BeanCoper;
import com.common.util.StringUtils;
import com.common.web.IExecute;
import com.passport.domain.module.AgentTypeEnum;
import com.passport.rpc.UserRPCService;
import com.passport.rpc.dto.ProxyDto;
import com.passport.rpc.dto.UserDTO;
import com.passport.rpc.dto.UserExtendDTO;
import com.passport.service.ClientUserInfoService;
import com.passport.service.SoftWareService;
import com.passport.web.AbstractClientController;
import com.passport.web.controller.dto.*;
import io.swagger.annotations.ApiOperation;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;


@Controller
@RequestMapping(value = "/login", method = {RequestMethod.POST})
public class LoginController extends AbstractClientController {

    @Resource
    private ClientUserInfoService loginService;

    @Resource
    private SoftWareService softWareService;
    @Value("${app.cookie.encode.key}")
    private String cookieEncodeKey;

    @RequestMapping("in")
    @ResponseBody
    @ApiOperation(value = "密码登录")
    public Map<String, Object> login(@RequestBody LoginByPassDto dto, HttpServletResponse response) {
        return buildMessage(new IExecute() {
            @Override
            public Object getData() {
                getRequest().getSession().removeAttribute("userDto");
                UserDTO login = loginService.login(getIP(), getDomain().getId(), dto.getAccount(), dto.getPass());
                putCookie("cToken", login.getToken(), response);
                return login;
            }
        });
    }

    @RequestMapping("buildLoginMobileCode")
    @ResponseBody
    @ApiOperation(value = "手机号登陆获取验证码")
    public Map<String, Object> buildLoginMobileCode(@RequestBody LoginByCodeDto dto) {
        return buildMessage(() -> {
            loginService.loginCodeBuild(getDomain().getId(), dto.getAccount());
            return null;
        });
    }

    @RequestMapping("loginByCodeVer")
    @ResponseBody
    @ApiOperation(value = "手机号登陆校验")

    public Map<String, Object> loginByCodeVer(@RequestBody LoginByCodeVerDto dto) {
        return buildMessage(new IExecute() {
            @Override
            public Object getData() {
                return loginService.loginCodeBuildVerification(getIP(), getDomain().getId(), dto.getAccount(), dto.getCode());
            }
        });
    }

    @RequestMapping("register")
    @ResponseBody
    @ApiOperation(value = "手机号注册获取验证码")
    public Map<String, Object> register(@RequestBody RegisterDto dto) {
        return buildMessage(() -> {
            loginService.regist(getDomain().getId(), dto.getAccount());
            return null;
        });
    }

    @RequestMapping("registVerification")
    @ResponseBody
    @ApiOperation(value = "手机号注册校验")
    public Map<String, Object> registVerification(@RequestBody RegisterVerDto dto) {
        return buildMessage(new IExecute() {
            @Override
            public Object getData() {
                return loginService.registVerification(getDomain(), dto.getAccount(), dto.getCode(), dto.getPass(), getIP());
            }
        });
    }

    @RequestMapping("loginOut")
    @ResponseBody
    @ApiOperation(value = "登出")
    public Map<String, Object> loginOut() {
        return buildMessage(() -> {
            try {
                UserDTO dto = getUserDto();
                if (dto == null) {
                    return true;
                }
                loginService.loginOut(dto.getPin(), dto.getToken());
            } catch (Exception e) {
                return false;
            }
            return true;
        });
    }


    @RequestMapping("check")
    @ApiOperation(value = "检查token是否有效")
    public Map<String, Object> check() {
        return buildMessage(() -> {
            try {
                String token = getUserDto().getToken();
                if (StringUtils.isBlank(token)) {
                    throw new BizException("token.error", "获取用户失败");
                }
            } catch (Exception e) {
                return false;
            }
            return true;
        });
    }

    @RequestMapping("changeMobile")
    @ApiOperation(value = "修改手机号获取验证码")
    public Map<String, Object> changeMobile(@RequestBody ChangeMobileDto dto) {
        return buildMessage(() -> {
            loginService.changeMobile(getDomain().getId(), getUserDto().getPin(), dto.getMobile());
            return null;
        });
    }

    @RequestMapping("changeMobileVer")
    @ApiOperation(value = "修改手机号校验")
    public Map<String, Object> changeMobileVer(@RequestBody ChangeMobileVerDto dto) {
        return buildMessage(() -> {
            loginService.changeMobile(getDomain().getId(), getUserDto().getPin(), dto.getMobile(), dto.getCode());
            return null;
        });
    }

    @RequestMapping("bindMobile")
    @ApiOperation(value = "绑定手机号获取验证码")
    public Map<String, Object> bindMobile(@RequestBody BindMobileDto dto) {
        return buildMessage(() -> {
            loginService.bindMobile(getDomain().getId(), getUserDto().getPin(), dto.getMobile());
            return null;
        });
    }

    @RequestMapping("bindMobileVer")
    @ApiOperation(value = "绑定手机号校验")
    public Map<String, Object> bindMobileVer(@RequestBody BindMobileVerDto dto) {
        return buildMessage(() -> {
            loginService.bindMobile(getDomain().getId(), getUserDto().getPin(), dto.getMobile(), dto.getCode());
            return null;
        });
    }

    @RequestMapping("changePass")
    @ApiOperation(value = "修改密码")
    public Map<String, Object> changePass(@RequestBody ChangePassDto dto) {
        return buildMessage(() -> {
            loginService.changePass(getDomain().getId(), getUserDto().getPin(), dto.getOldPass(), dto.getNewPass());
            return null;
        });
    }

    @RequestMapping("changeNick")
    @ApiOperation(value = "修改昵称")
    public Map<String, Object> changeNick(@RequestBody ChangeNickDto dto) {
        return buildMessage(() -> {
            loginService.changeNickName(getDomain().getId(), getUserDto().getPin(), dto.getNick());
            return null;
        });
    }

    @RequestMapping("changeSex")
    @ApiOperation(value = "修改性别")
    public Map<String, Object> changeSex(@RequestBody ChangeSexDto dto) {
        return buildMessage(() -> {
            loginService.changeSex(getDomain().getId(), getUserDto().getPin(), dto.getSex());
            return null;
        });
    }

    @RequestMapping("changeBirthday")
    @ApiOperation(value = "修改生日")
    public Map<String, Object> changeBirthday(@RequestBody String birthday) {
        return buildMessage(() -> {
            loginService.changeBirthday(getDomain().getId(), getUserDto().getPin(), JSONObject.fromObject(birthday).getString("birthday"));
            return null;
        });
    }

    @RequestMapping("forgetPass")
    @ApiOperation(value = "忘记密码获取验证码")
    public Map<String, Object> forgetPass(@RequestBody String account) {
        return buildMessage(() -> {
            loginService.forgetPass(getDomain().getId(), JSONObject.fromObject(account).getString("account"));
            return null;
        });
    }

    @RequestMapping("forgetPassVer")
    @ApiOperation(value = "忘记密码校验")
    public Map<String, Object> forgetPassVer(@RequestBody ForgetPassVerDto dto) {
        return buildMessage(() -> {
            loginService.forgetPassCodeVerification(getDomain().getId(), dto.getAccount(), dto.getCode(), dto.getPass());
            return null;
        });
    }

    @RequestMapping("saveExtendInfo")
    @ApiOperation(value = "保存扩展信息")
    public Map<String, Object> saveExtendInfo(@RequestBody UserExtendDto dto) {
        UserExtendDTO userExtendDTO = new UserExtendDTO();
        BeanCoper.copyProperties(userExtendDTO, dto);
        return buildMessage(() -> {
            loginService.saveUserExtendInfo(getDomain().getId(), userExtendDTO);
            return null;
        });
    }


    @RequestMapping(value = "reg", method = {RequestMethod.GET})
    @ApiOperation(value = "用户注册")
    public String reg(String q, Model model) {
        model.addAttribute("recommendId", q);
        String domain = StringUtils.getDomain(getRequest().getRequestURL().toString());
        AgentTypeEnum agentType = getAgentType();
        if (agentType == AgentTypeEnum.Android || agentType == AgentTypeEnum.Other) {
            model.addAttribute("url", "http://image." + domain + softWareService.findLastInfo(getDomain().getId(), AgentTypeEnum.Android).getUrl());
        }
        if (agentType == AgentTypeEnum.IOS) {

            model.addAttribute("url", "itms-services://?action=download-manifest&url=http://passport." + domain + "/AppDownload/download.plist");
        }
        return "/register";
    }

    public AgentTypeEnum getAgentType() {
        String agent = getRequest().getHeader("user-agent").toLowerCase();
        if (agent.indexOf(AgentTypeEnum.Android.name().toLowerCase()) != -1) {
            return AgentTypeEnum.Android;
        }
        if (agent.indexOf("iPhone".toLowerCase()) != -1 || agent.indexOf("iPod".toLowerCase()) != -1 || agent.indexOf("iPad".toLowerCase()) != -1) {
            return AgentTypeEnum.IOS;
        }
        return AgentTypeEnum.Other;
    }
}
