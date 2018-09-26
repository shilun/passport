package com.passport.web.controller;

import com.passport.rpc.dto.UserDTO;
import com.passport.service.ClientUserInfoService;
import com.passport.service.constant.HttpStatusCode;
import com.passport.service.util.OldPackageMapUtil;
import com.passport.web.AbstractClientController;
import com.passport.web.controller.dto.*;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Luo
 * @date 2018/9/21 16:56
 */
@Controller
@RequestMapping(value = "/appinterface", method = {RequestMethod.POST})
public class OldController extends AbstractClientController {
    @Resource
    private ClientUserInfoService clientService;



    @RequestMapping("getValidateCode")
    @ResponseBody
    @ApiOperation(value = "获取验证码")
    public Map<String, Object> getValidateCode(@RequestBody OldGetCodeDto dto, HttpServletResponse response) {
        return clientService.getValidateCode(getDomain().getId(),dto.getPhoneNo(),dto.getCodeType());
    }


    @RequestMapping("user-reg")
    @ResponseBody
    @ApiOperation(value = "用户注册")
    public Map<String, Object> userRegister(@RequestBody OldRegDto dto, HttpServletResponse response) {
        UserDTO userDTO = clientService.registVerification(getDomain(), dto.getAccessName(), dto.getValidateCode(), dto.getAccessToken(), getIP());
        if(userDTO == null){
            return OldPackageMapUtil.toFailMap(HttpStatusCode.CODE_BAD_REQUEST,"注册失败");
        }
        return OldPackageMapUtil.toSuccessMap(HttpStatusCode.CODE_OK,HttpStatusCode.MSG_OK);
    }

    @RequestMapping("user-login")
    @ResponseBody
    @ApiOperation(value = "用户登陆")
    public Map<String, Object> userLogin(@RequestBody OldLoginDto dto,HttpServletResponse response) {
        if(dto.getType() != 1){
            return OldPackageMapUtil.toFailMap(HttpStatusCode.CODE_BAD_REQUEST,"登陆类型错误");
        }
        Map<String, Object> map = null;
        try{
            UserDTO userDto = clientService.login(getIP(), getDomain().getId(), dto.getLoginName(), dto.getPwd());
            map = new HashMap<>();
            map.put("gateServerTicket",userDto.getToken());
            map.put("playerId",userDto.getId());
            Object[] arr = new Object[1];
            Map<String, Object> hallMap = new HashMap<>();
            hallMap.put("server_id","200001");
            hallMap.put("server_type",2);
            hallMap.put("safe_ips","192.168.31.47");
            hallMap.put("evironment","development");
            hallMap.put("server_name","Gate服务");
            hallMap.put("isopen",1);
            hallMap.put("game_id",-1);
            hallMap.put("webport",8019);
            hallMap.put("tcpport",34100);
            arr[0] = hallMap;
            map.put("hall_list",arr);
        }catch (Exception e){
            return OldPackageMapUtil.toFailMap(HttpStatusCode.CODE_BAD_REQUEST,e.getMessage());
        }
        return OldPackageMapUtil.toMap(HttpStatusCode.CODE_OK,HttpStatusCode.MSG_OK,true,map);
    }

    @RequestMapping("updateLoninPwd")
    @ResponseBody
    @ApiOperation(value = "修改登陆密码")
    public Map<String, Object> updateLoninPwd(@RequestBody OldUpdatePwdDto dto, HttpServletResponse response) {
        return clientService.oldUpdatePwd(getDomain().getId(),dto.getAccessName(),dto.getPwd(),dto.getNewPwd());
    }

    @RequestMapping("forgetLoninPwd")
    @ResponseBody
    @ApiOperation(value = "忘记密码")
    public Map<String, Object> forgetPwd(@RequestBody OldForgetPwd dto, HttpServletResponse response) {
        return clientService.oldForgetPass(getDomain().getId(),dto.getAccessName(),dto.getValidateCode(),dto.getPwd());
    }

    @RequestMapping("getUser")
    @ResponseBody
    @ApiOperation(value = "根据userId查询用户")
    public Map<String, Object> getUserById(@RequestBody OldGetUserByIdDto dto, HttpServletResponse response) {
        return clientService.oldFindByUserCode(getDomain().getId(),dto.getUserCode());
    }

    @RequestMapping("getUserByAccessName")
    @ResponseBody
    @ApiOperation(value = "根据Account查询用户")
    public Map<String, Object> getUserByAccount(@RequestBody OldGetUserByAccountDto dto, HttpServletResponse response) {
        return clientService.oldFindByAccount(getDomain().getId(),dto.getAccessName());
    }

    @RequestMapping("editPlayerInfo")
    @ResponseBody
    @ApiOperation(value = "修改用户信息")
    public Map<String, Object> editUserInfo(@RequestBody OldEditUserInfoDto dto, HttpServletResponse response) {
        return clientService.oldEditUserInfo(getDomain().getId(),dto.getUserId(),dto.getNick(),dto.getQq(),dto.getWechat(),dto.getGender(),dto.getSign());
    }

    @RequestMapping("certification")
    @ResponseBody
    @ApiOperation(value = "实名认证")
    public Map<String, Object> certification(@RequestBody OldCertificationDto dto, HttpServletResponse response) {
        return clientService.OldCertification(getDomain().getId(),dto.getUserId(),dto.getRealName(),dto.getIdCard());
    }
}
