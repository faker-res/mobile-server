package com.lzkj.mobile.v2.controller;

import com.lzkj.mobile.client.AccountsServiceClient;
import com.lzkj.mobile.config.SystemConstants;
import com.lzkj.mobile.exception.GlobeException;
import com.lzkj.mobile.util.TimeUtil;
import com.lzkj.mobile.v2.service.PersonCenterService;
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

/**
 *    
 *  *  
 *  * @Project: mobile-server 
 *  * @Package: com.lzkj.mobile.v2.controller 
 *  * @Description: TODO   
 *  * @Author:   horus   
 *  * @CreateDate:  2020/2/11 19:33  
 *  * @Version:   v1.0
 *  *    
 *  
 */
@Slf4j
@RestController
@Api(value = "PersonCenterController", tags = "个人中心接口")
public class PersonCenterController {

    @Resource
    private AccountsServiceClient accountsServiceClient;
    @Resource
    private PersonCenterService personCenterService;

    @GetMapping("/mobileInterface/getUserVipLevel")
    @ApiOperation(value = "查询玩家VIP等级", notes = "查询玩家VIP等级")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "s", value = "签名", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "userId", value = "用户ID", paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "parentId", value = "业主ID", paramType = "query", dataType = "int")
    })
    public GlobeResponse<Map<String, Object>> getUserVipLevel(@RequestParam Integer userId, Integer parentId) {
        GlobeResponse<Map<String, Object>> globeResponse = new GlobeResponse<>();
        Map<String, Object> data = personCenterService.getUserVipLevel(userId, parentId);
        globeResponse.setData(data);
        return globeResponse;
    }

    @GetMapping("/mobileInterface/getVipUserInfo")
    @ApiOperation(value = "获取用户个人信息", notes = "获取用户个人信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "s", value = "签名", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "userId", value = "用户ID", paramType = "query", dataType = "int"),
    })
    public GlobeResponse<UserInformationVO> getVipUserInfo(@RequestParam Integer userId) {
        GlobeResponse<UserInformationVO> globeResponse = new GlobeResponse<>();
        UserInformationVO userInfo = accountsServiceClient.getUsersInfo(userId);
        globeResponse.setData(userInfo);
        return globeResponse;
    }

}
