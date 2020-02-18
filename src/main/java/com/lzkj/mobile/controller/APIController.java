package com.lzkj.mobile.controller;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lzkj.mobile.client.PlatformServiceClient;
import com.lzkj.mobile.config.SystemConstants;
import com.lzkj.mobile.exception.GlobeException;
import com.lzkj.mobile.redis.RedisDao;
import com.lzkj.mobile.redis.RedisKeyPrefix;
import com.lzkj.mobile.vo.GlobeResponse;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/api")
@Api(tags = "api接口")
public class APIController {
	
	@Autowired
	private PlatformServiceClient platformServiceClient;
	
	@Autowired
	private RedisDao redisService;
	
	@RequestMapping("/isOrNotEnter")
	@ApiOperation(value = "用户是否能够进入游戏", notes = "用户是否能够进入游戏")
    @ApiImplicitParams ({
        @ApiImplicitParam(name = "s", value = "签名", paramType = "query", dataType = "String"),
        @ApiImplicitParam(name = "agentId", value = "业主ID", paramType = "query", dataType = "int", required = true),
        @ApiImplicitParam(name = "kindId", value = "游戏ID", paramType = "query", dataType = "int", required = true)
    })
	public GlobeResponse<Object> isOrNotEnter(Integer agentId,Integer kindId){
		if(agentId == null || agentId == 0 || kindId == null || kindId == 0) {
			throw new GlobeException(SystemConstants.FAIL_CODE, "参数错误");
		}
		String redisKey = RedisKeyPrefix.getNullity(agentId, kindId);
		Boolean flag = redisService.get(redisKey, Boolean.class);
		if(null == flag) {
			flag = platformServiceClient.getisOrNotEnter(agentId,kindId);
			redisService.set(redisKey, flag);
			redisService.expire(redisKey, 2, TimeUnit.HOURS);
		}
		GlobeResponse<Object> globeResponse = new GlobeResponse<Object>();
		globeResponse.setData(flag);
		return globeResponse;
	}
	
}