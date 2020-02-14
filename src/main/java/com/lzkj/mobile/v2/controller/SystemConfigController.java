package com.lzkj.mobile.v2.controller;

import com.lzkj.mobile.client.AccountsServiceClient;
import com.lzkj.mobile.client.NativeWebServiceClient;
import com.lzkj.mobile.config.AgentSystemEnum;
import com.lzkj.mobile.config.SystemConstants;
import com.lzkj.mobile.exception.GlobeException;
import com.lzkj.mobile.redis.JsonUtil;
import com.lzkj.mobile.redis.RedisKeyPrefix;
import com.lzkj.mobile.v2.service.SystemConfigService;
import com.lzkj.mobile.vo.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 *    
 *  *  
 *  * @Project: mobile-server 
 *  * @Package: com.lzkj.mobile.v2.controller 
 *  * @Description: TODO   
 *  * @Author:   horus   
 *  * @CreateDate:  2020/2/11 19:01  
 *  * @Version:   v1.0
 *  *    
 *  
 */
@Slf4j
@RestController
@Api(tags = "系统配置接口")
public class SystemConfigController {

    @Resource
    private SystemConfigService systemConfigService;

    @GetMapping("/agentSystem/newLoginStatus")
    @ApiOperation(value = "获取二维码和热更地址", notes = "获取二维码和热更地址")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "s", value = "签名", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "agentId", value = "业主ID", paramType = "query", dataType = "int", required = true),
            @ApiImplicitParam(name = "registerMachine", value = "注册机器", paramType = "query", dataType = "String")
    })
    public Map<String, Object> getNewLoginStatus(@RequestParam Integer agentId, String registerMachine) {
        return systemConfigService.getNewLoginStatus(agentId, registerMachine);
    }

}
