package com.lzkj.mobile.v2.controller;

import com.lzkj.mobile.client.AccountsServiceClient;
import com.lzkj.mobile.client.NativeWebServiceClient;
import com.lzkj.mobile.config.SystemConstants;
import com.lzkj.mobile.exception.GlobeException;
import com.lzkj.mobile.vo.GlobeResponse;
import com.lzkj.mobile.vo.NewsVO;
import com.lzkj.mobile.vo.SystemNewsVO;
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
import java.util.List;

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
@Api(value = "CommonController", tags = "公共(公告/轮播等)接口")
public class CommonController {

    @Resource
    private AccountsServiceClient accountsServiceClient;
    @Resource
    private NativeWebServiceClient nativeWebServiceClient;

    @GetMapping("/mobileInterface/getGameNews")
    @ApiOperation(value = "游戏公告", notes = "游戏公告")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "s", value = "签名", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "agentId", value = "业主ID", paramType = "query", dataType = "int")
    })
    public GlobeResponse<List<SystemNewsVO>> getGameNews(@RequestParam Integer agentId) {
        GlobeResponse<List<SystemNewsVO>> globeResponse = new GlobeResponse<>();
        globeResponse.setData(accountsServiceClient.getGameNews(agentId));
        return globeResponse;
    }

    @GetMapping("/mobileInterface/getNoticeTitile")
    @ApiOperation(value = "游戏公告", notes = "游戏公告")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "s", value = "签名", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "agentId", value = "业主ID", paramType = "query", dataType = "int")
    })
    public GlobeResponse<List<NewsVO>> getNoticeTitile(@RequestParam Integer agentId) {
        GlobeResponse<List<NewsVO>> globeResponse = new GlobeResponse<>();
        globeResponse.setData(nativeWebServiceClient.getNoticeTitile(1, agentId));
        return globeResponse;
    }

}
