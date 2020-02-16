package com.lzkj.mobile.controller;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lzkj.mobile.client.NativeWebServiceClient;
import com.lzkj.mobile.config.SystemConstants;
import com.lzkj.mobile.exception.GlobeException;
import com.lzkj.mobile.redis.RedisDao;
import com.lzkj.mobile.redis.RedisKeyPrefix;
import com.lzkj.mobile.util.HttpRequest;
import com.lzkj.mobile.util.TimeUtil;
import com.lzkj.mobile.vo.ActivityRecordVO;
import com.lzkj.mobile.vo.ActivityReviewRecordVO;
import com.lzkj.mobile.vo.CommonPageVO;
import com.lzkj.mobile.vo.GlobeResponse;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/active")
@Slf4j
public class ActiveCenterController {

    @Autowired
    private NativeWebServiceClient nativeWebServiceClient;
    
    @Value("${server.url}")
    private String serverUrl;
    
    @Autowired
    private RedisDao redisService;
    
    /**
     * 玩家手动申请活动
     */
    @RequestMapping("/userApply")
    public GlobeResponse userApply(Integer userId, Integer activeId, BigDecimal amount, Integer agentId,String memo) {
        if (activeId == 0 || userId == 0 || amount == null) {
            throw new GlobeException(SystemConstants.FAIL_CODE, "请先登录");
        }
        Map<String, Object> param = nativeWebServiceClient.userApply(userId, activeId, amount,agentId,memo);
        GlobeResponse globeResponse = new GlobeResponse();
        globeResponse.setData(param);
        return globeResponse;
    }

    /**
     * 查看玩家申请的记录
     */
    @RequestMapping("/getApplyList")
    public GlobeResponse getApplyList(Integer userId, Integer pageIndex, Integer pageSize,Integer kindType) {
        if (pageIndex == 0 || userId == 0 || pageSize == null) {
            throw new GlobeException(SystemConstants.FAIL_CODE, "参数错误");
        }

        CommonPageVO<ActivityRecordVO> pageVO =  nativeWebServiceClient.getApplyList(userId,pageIndex,pageSize,kindType);
        Map<String,Object> param = new HashMap<>();
        param.put("list",pageVO.getLists());
        param.put("record",pageVO.getRecordCount());
        GlobeResponse globeResponse = new GlobeResponse();
        globeResponse.setData(param);
        return globeResponse;
    }
    
    /**
     * 用户触发活动
     */
    @RequestMapping("/getActivityByTrigger")
    public GlobeResponse<Object> getActivityByTrigger(Integer userId,Integer agentId,Integer method,Integer device){
    	if(userId == 0 || agentId == null || userId == null || agentId == 0 || method == null || method == 0 || device == 0 || device == null) {
    		throw new GlobeException(SystemConstants.FAIL_CODE, "参数错误");
    	}
    	GlobeResponse<Object> globeResponse = new GlobeResponse<Object>();
    	globeResponse.setData(nativeWebServiceClient.getActivityByTrigger(userId,agentId,method,device));
    	return globeResponse;
    }
    
    /**
     * 根据活动id显示用户的进度
     */
    @RequestMapping("/getAccountsActivity")
    public GlobeResponse<Object> getAccountsActivity(Integer userId,Integer activityId,Integer agentId,Integer ruleType){
    	if(null == userId || userId == 0 || null == activityId || activityId == 0 || null == agentId || agentId == 0 || null == ruleType || ruleType == 0) {
    		throw new GlobeException(SystemConstants.FAIL_CODE, "参数错误");
    	}
    	GlobeResponse<Object> globeResponse = new GlobeResponse<Object>();
    	globeResponse.setData(nativeWebServiceClient.getAccountsActivity(userId,activityId,agentId,ruleType));
    	return globeResponse;
    }
    
    @RequestMapping("/getActivityApplication")
    public GlobeResponse<Object> getActivityApplication(Integer activityId,Integer agentId){
    	if(null == activityId || activityId == 0 || null == agentId || agentId == 0) {
    		throw new GlobeException(SystemConstants.FAIL_CODE, "参数错误");
    	}
    	GlobeResponse<Object> globeResponse = new GlobeResponse<Object>();
    	globeResponse.setData(nativeWebServiceClient.getActivityParameter(activityId,agentId));
    	return globeResponse;
    }
    
    @RequestMapping("/getAccountsApplication")
    public GlobeResponse<Object> getAccountsApplication(Integer id,Integer userId,Integer activityId,Integer agentId,Integer ruleType,Integer ruleId,HttpServletRequest request){
    	if(null == activityId || activityId == 0 || null == agentId || agentId == 0 || null == userId || userId == 0 || null == id || id == 0 || null == ruleType || ruleType == 0 || null == ruleId || ruleId == 0) {
    		throw new GlobeException(SystemConstants.FAIL_CODE, "参数错误");
    	}
    	String redisKey = RedisKeyPrefix.getActivityApplication(userId, ruleId);
    	if(!StringUtils.isBlank(redisService.get(redisKey, String.class))){
    		throw new GlobeException(SystemConstants.FAIL_CODE, "操作太频繁");
    	}
    	redisService.set(redisKey, "lock");
    	redisService.expire(redisKey, 3, TimeUnit.SECONDS);
    	String appDate = TimeUtil.getNow();
    	String ip = request.getRemoteHost();
    	GlobeResponse<Object> globeResponse = new GlobeResponse<Object>();
    	String str = nativeWebServiceClient.getAccountsApplication(id,userId,activityId,agentId,ruleType,ruleId,appDate,ip);
    	String [] args = str.split("!");
    	globeResponse.setMsg(args[0]);
    	if(args.length > 1) {
    		String msg = "{\"msgid\":7,\"userId\":" + userId + ", \"score\":" + args[1] + ",\"insuranceScore\":" + 0 +
                    ", \"VipLevel\":" + args[2] + ", \"type\":" + 0 + ", \"Charge\":" + 0 + "}";
            log.info("调用金额变更指令:{}, 返回：" + HttpRequest.sendPost(this.serverUrl, msg), msg);
    	}
    	redisService.delete(redisKey);
    	return globeResponse;
    }
    
    @RequestMapping("/getActivityReviewRecordByUser")
    public GlobeResponse<Object> getActivityReviewRecordByUser(Integer userId,Integer agentId,Integer pageIndex,Integer status){
    	if(null == userId || userId == 0 || null == agentId || agentId == 0 || null == status || status < -1) {
    		throw new GlobeException(SystemConstants.FAIL_CODE, "参数错误");
    	}
    	if(null == pageIndex || pageIndex <= 0) {
    		pageIndex = 1;
    	}
    	CommonPageVO<ActivityReviewRecordVO> page = nativeWebServiceClient.getActivityReviewRecordByUser(userId,agentId,pageIndex,status);
    	GlobeResponse<Object> globeResponse = new GlobeResponse<Object>();
    	Map<String,Object> map = new HashMap<String, Object>();
    	map.put("list", page.getLists());
    	map.put("count", page.getRecordCount());
    	globeResponse.setData(map);
    	return globeResponse;
    }
}
