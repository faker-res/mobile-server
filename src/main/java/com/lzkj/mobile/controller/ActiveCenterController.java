package com.lzkj.mobile.controller;

import com.lzkj.mobile.client.NativeWebServiceClient;
import com.lzkj.mobile.config.SystemConstants;
import com.lzkj.mobile.exception.GlobeException;
import com.lzkj.mobile.vo.ActivityRecordVO;
import com.lzkj.mobile.vo.CommonPageVO;
import com.lzkj.mobile.vo.GlobeResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/active")
@Slf4j
public class ActiveCenterController {

    @Autowired
    private NativeWebServiceClient nativeWebServiceClient;

    /**
     * 玩家手动申请活动
     */
    @RequestMapping("/userApply")
    private GlobeResponse userApply(Integer userId, Integer activeId, BigDecimal amount, Integer agentId,String memo) {
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
    private GlobeResponse getApplyList(Integer userId, Integer pageIndex, Integer pageSize,Integer kindType) {
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
}
