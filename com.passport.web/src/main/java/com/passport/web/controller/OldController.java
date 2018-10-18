package com.passport.web.controller;

import com.passport.domain.ProxyServerInfo;
import com.passport.domain.module.GameTypeEnum;
import com.passport.rpc.dto.UserDTO;
import com.passport.service.ClientUserInfoService;
import com.passport.service.ProxyServerInfoService;
import com.passport.service.constant.HttpStatusCode;
import com.passport.service.util.OldPackageMapUtil;
import com.passport.web.AbstractClientController;
import com.passport.web.controller.dto.*;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
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
    @Resource
    private ProxyServerInfoService proxyServerInfoService;

    @Value("${app.upload.scode}")
    private String scode;
    @Value("${app.upload.domain}")
    private String uploadDomain;

    @RequestMapping("forgetPassBuildCode")
    @ResponseBody
    @ApiOperation(value = "忘记密码获取验证码")
    public Map<String, Object> forgetPassBuildCode(@RequestBody OldGetCodeDto dto) {
        return clientService.oldForgetPassBuildCode(getDomain().getId(),dto.getPhoneNo());
    }
    @RequestMapping("regBuildCode")
    @ResponseBody
    @ApiOperation(value = "注册获取验证码")
    public Map<String, Object> regBuildCode(@RequestBody OldGetCodeDto dto) {
        return clientService.oldRegistBuildCode(getDomain().getId(),dto.getPhoneNo());
    }

    @RequestMapping("user-reg")
    @ResponseBody
    @ApiOperation(value = "用户注册")
    public Map<String, Object> userRegister(@RequestBody OldRegDto dto) {
        return clientService.oldRegist(getDomain(), dto.getAccessName(), dto.getValidateCode(), dto.getAccessToken(), getIP(),null);
    }


    @RequestMapping(value = "reg", method = {RequestMethod.GET})
    @ApiOperation(value = "用户注册")
    public String reg(String recommendId,Model model) {
        model.addAttribute("recommendId",recommendId);
        return "/register";
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
            getRequest().getSession().removeAttribute("userDto");
            Long proxyId = getDomain().getId();
            UserDTO userDto = clientService.login(getIP(), proxyId, dto.getLoginName(), dto.getPwd());
            putCookie("cToken", userDto.getToken(), response);
            map = new HashMap<>();
            map.put("gateServerTicket",userDto.getToken());
            map.put("playerId",userDto.getId());
            ProxyServerInfo proxyServerInfo = new ProxyServerInfo();
            proxyServerInfo.setProxyId(proxyId);
            proxyServerInfo.setGameType(GameTypeEnum.Qipai.getValue());
            List<ProxyServerInfo> list = proxyServerInfoService.query(proxyServerInfo);
            int len = list.size();
            Object[] arr = new Object[len];
            for(int i=0;i<len;i++){
                Map<String, Object> hallMap = new HashMap<>();
                ProxyServerInfo info = list.get(i);
                hallMap.put("server_id",info.getServerId());
                hallMap.put("server_type",2);
                hallMap.put("safe_ips",info.getIp());
                hallMap.put("evironment",info.getEvironment());
                hallMap.put("server_name","Gate服务");
                hallMap.put("isopen",info.getStatus());
                hallMap.put("game_id",-1);
                hallMap.put("webport",8019);
                hallMap.put("tcpport",info.getPort());
                arr[i] = hallMap;
            }
            map.put("hall_list",arr);
        }catch (Exception e){
            return OldPackageMapUtil.toFailMap(HttpStatusCode.CODE_BAD_REQUEST,e.getMessage());
        }
        return OldPackageMapUtil.toMap(HttpStatusCode.CODE_OK,HttpStatusCode.MSG_OK,true,map);
    }

    @RequestMapping("updateLoninPwd")
    @ResponseBody
    @ApiOperation(value = "修改登陆密码")
    public Map<String, Object> updateLoninPwd(@RequestBody OldUpdatePwdDto dto) {
        UserDTO userDTO=getUserDto();
        if(userDTO == null){
            return OldPackageMapUtil.toFailMap(HttpStatusCode.CODE_BAD_REQUEST,"登陆信息过期");
        }
        return clientService.oldUpdatePwd(userDTO.getProxyId(),userDTO.getPin(),dto.getPwd(),dto.getNewPwd());
    }

    @RequestMapping("forgetLoninPwd")
    @ResponseBody
    @ApiOperation(value = "忘记密码")
    public Map<String, Object> forgetPwd(@RequestBody OldForgetPwd dto) {
        return clientService.oldForgetPass(getDomain().getId(),dto.getAccessName(),dto.getValidateCode(),dto.getPwd());
    }

    @RequestMapping("editPlayerInfo")
    @ResponseBody
    @ApiOperation(value = "修改用户信息")
    public Map<String, Object> editUserInfo(@RequestBody OldEditUserInfoDto dto) {
        UserDTO userDTO=getUserDto();
        if(userDTO == null){
            return OldPackageMapUtil.toFailMap(HttpStatusCode.CODE_BAD_REQUEST,"登陆信息过期");
        }
        return clientService.oldEditUserInfo(getDomain().getId(),userDTO.getPin(),dto.getNick(),dto.getQq(),dto.getWechat(),dto.getGender(),dto.getSign());
    }

    @RequestMapping("certification")
    @ResponseBody
    @ApiOperation(value = "实名认证")
    public Map<String, Object> certification(@RequestBody OldCertificationDto dto) {
        UserDTO userDTO=getUserDto();
        if(userDTO == null){
            return OldPackageMapUtil.toFailMap(HttpStatusCode.CODE_BAD_REQUEST,"登陆信息过期");
        }
        return clientService.OldCertification(getDomain().getId(),userDTO.getPin(),dto.getRealName(),dto.getIdCard());
    }

    @RequestMapping("getUserQrCode")
    @ResponseBody
    @ApiOperation(value = "获取用户二维码")
    public Map<String, Object> getUserQrCode(){
        UserDTO userDTO=getUserDto();
        if(userDTO == null){
            return OldPackageMapUtil.toFailMap(HttpStatusCode.CODE_BAD_REQUEST,"登陆信息过期");
        }
        StringBuffer sb = new StringBuffer();
        sb.append("http://").append(uploadDomain).append("/").append(scode).append("/").append(userDTO.getQrName());
        return OldPackageMapUtil.toSuccessMap(HttpStatusCode.CODE_OK,"查询成功",sb.toString());
    }

    @RequestMapping("regByQr")
    @ResponseBody
    @ApiOperation(value = "二维码注册")
    public Map<String, Object> regByQr(@RequestBody OldQRRegDto dto){
        return clientService.oldRegist(getDomain(), dto.getAccessName(), dto.getValidateCode(), dto.getAccessToken(), getIP(),dto.getRecommendId());
    }
}
