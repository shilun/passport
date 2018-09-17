package com.passport.web.controller;

import com.common.util.RPCResult;
import com.integration.api.AbstractClientController;
import com.integration.service.AccountService;
import com.passport.rpc.dto.UserDTO;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @author Luo
 * @date 2018/9/4 18:07
 */
@RestController
@RequestMapping(value = "/account", method = {RequestMethod.POST})
public class AccountController extends AbstractClientController {
    @Resource
    private AccountService accountService;

    @RequestMapping("findAccount")
    @ApiOperation(value = "查询账户")
    public Map<String,Object> findAccount() {
        return buildMessage(() -> accountService.queryAccount(getUserDto().getPin(), getDomain().getId()));
    }
}
