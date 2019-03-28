package com.passport.health;

import com.alibaba.dubbo.config.annotation.Reference;
import com.common.health.AbstractHealThIndicator;
import com.common.rpc.StatusRpcService;
import com.passport.rpc.UserRPCService;
import com.platform.rpc.RecommendRPCService;
import org.springframework.stereotype.Component;

@Component
public class Platfrom extends AbstractHealThIndicator {

    @Reference(check = false)
    private RecommendRPCService recommendRPCService;

    @Override
    protected StatusRpcService getStatusRpcService() {
        return recommendRPCService;
    }
}
