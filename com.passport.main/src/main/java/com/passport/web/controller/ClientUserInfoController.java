package com.passport.web.controller;

import com.common.util.BeanCoper;
import com.common.web.IExecute;
import com.passport.domain.ClientUserInfo;
import com.passport.service.ClientUserInfoService;
import com.passport.web.AbstractClientController;
import com.passport.web.controller.dto.ClientUserDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;

@Api(description = "用户管理")
@RestController
@RequestMapping(method = {RequestMethod.POST})
public class ClientUserInfoController extends AbstractClientController {
    @Resource
    private ClientUserInfoService clientUserInfoService;

    /**
     * 查询
     *
     * @param info
     * @return
     */
    @ApiOperation(value = "查询")
    @RequestMapping("/user/list")
    @ResponseBody
    public Map<String, Object> list(@RequestBody ClientUserDto info) {
        return buildMessage(new IExecute() {
            @Override
            public Object getData() {
                ClientUserInfo entity=new ClientUserInfo();
                BeanCoper.copyProperties(entity,info);
                return clientUserInfoService.queryByPage(entity,info.getPageinfo().getPage());
            }
        });
    }
    /**
     * 查询
     *
     * @param content
     * @return
     */
    @ApiOperation(value = "保存")
    @RequestMapping("/user/view")
    @ResponseBody
    public Map<String, Object> view(@RequestBody String content) {
        return buildMessage(new IExecute() {
            @Override
            public Object getData() {
                return clientUserInfoService.findById(getIdByJson(content));
            }
        });
    }
    /**
     * 保存
     *
     * @param info
     * @return
     */
    @ApiOperation(value = "保存")
    @RequestMapping("/user/save")
    @ResponseBody
    public Map<String, Object> save(@RequestBody ClientUserDto info) {
        return buildMessage(new IExecute() {
            @Override
            public Object getData() {
                ClientUserInfo entity=new ClientUserInfo();
                BeanCoper.copyProperties(entity,info);
                return clientUserInfoService.save(entity);
            }
        });
    }
}
