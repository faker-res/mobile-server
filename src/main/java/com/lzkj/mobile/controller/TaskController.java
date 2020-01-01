package com.lzkj.mobile.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lzkj.mobile.client.PlatformServiceClient;
import com.lzkj.mobile.config.SystemConstants;
import com.lzkj.mobile.exception.GlobeException;
import com.lzkj.mobile.util.TimeUtil;
import com.lzkj.mobile.vo.AccountsTask;
import com.lzkj.mobile.vo.GlobeResponse;
import com.lzkj.mobile.vo.TaskVO;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/taskController")
@Slf4j
public class TaskController {
	
	@Autowired
	private PlatformServiceClient platformServiceClient;
	
	@RequestMapping("/getTaskDetails")
	public GlobeResponse<TaskVO> getTaskDetails(Integer taskId,Integer userId) {
		GlobeResponse<TaskVO> gb = new GlobeResponse<TaskVO>();
		try {
			if(null == userId) {
				throw new GlobeException(SystemConstants.FAIL_CODE, "参数错误");
			}
			if(null == taskId) {
				throw new GlobeException(SystemConstants.FAIL_CODE, "参数错误");
			}
			TaskVO vo = platformServiceClient.getTaskDetails(taskId);
			StringBuffer sb = new StringBuffer();
			vo.setTaskExplain("限时推广任务，实时结算，实时可领。限时任务有效期内不重置更新。");
			List<String> list = TimeUtil.getFormatDate();
			if(vo.getTaskPeriod() == 4) {
				vo.setTaskBeginTime("");
    			vo.setTaskEndTime("");
    			vo.setTaskExplain("不限时推广任务，完成即可领取，实时结算。");
			}else {
				sb.append("任务有效期内");
				if(vo.getTaskPeriod() == 1) {
					vo.setTaskBeginTime(list.get(0));
					vo.setTaskEndTime(list.get(1));
	    		}else if(vo.getTaskPeriod() == 2) {
	    			vo.setTaskBeginTime(list.get(2));
	    			vo.setTaskEndTime(list.get(3));
	    		}
			}
			if(vo.getTaskType() == 1) {
				sb.append("充值达到"+vo.getInnings()+"元及以上");
			}else if(vo.getTaskType() == 2) {
				sb.append("打码达到"+vo.getInnings()+"元及以上");
			}else if(vo.getTaskType() == 3) {
				sb.append("损失达到"+vo.getInnings()+"元及以上");
			}else {
				sb.append("新增直属下级均需充值"+vo.getInnings()+"元及以上");
			}
			vo.setTaskRang(sb.toString());
			gb.setData(vo);
		} catch (Exception e) {
			log.info("获取任务详情失败",e);
		}
		return gb;
	}
	
	@RequestMapping("/getTaskinfoByUserId")
	public GlobeResponse<List<AccountsTask>> getTaskinfoByUserId(Integer userId,Integer agentId){
		GlobeResponse<List<AccountsTask>> gb = new GlobeResponse<List<AccountsTask>>();
		try {
			if(null == userId) {
				throw new GlobeException(SystemConstants.FAIL_CODE, "参数错误");
			}
			if(null == agentId) {
				throw new GlobeException(SystemConstants.FAIL_CODE, "参数错误");
			}
			List<AccountsTask> list = platformServiceClient.getTaskinfoByUserId(userId, agentId);
			gb.setData(list);
		} catch (Exception e) {
			log.info("获取用户对应的任务报错",e);
		}
		return gb;
	}
	
	@RequestMapping("/getTaskReward")
	public GlobeResponse<Object> getTaskReward(Integer userId,Integer taskId,String password,String machinIe,HttpServletRequest request){
		GlobeResponse<Object> gb = new GlobeResponse<Object>();
		try {
			if(null == userId) {
				throw new GlobeException(SystemConstants.FAIL_CODE, "参数错误");
			}
			if(null == taskId) {
				throw new GlobeException(SystemConstants.FAIL_CODE, "参数错误");
			}
			if(StringUtils.isBlank(password)) {
				throw new GlobeException(SystemConstants.FAIL_CODE, "参数错误");
			}
			if(StringUtils.isBlank(machinIe)) {
				throw new GlobeException(SystemConstants.FAIL_CODE, "参数错误");
			}
			String ip = request.getRemoteHost();
			String obj = platformServiceClient.getTaskReward(userId, taskId, password, machinIe, ip);			 
			gb.setData(obj);
		} catch (Exception e) {
			gb.setData("请求失败");
			log.info("用户"+userId+"领取奖励报错",e);
		}
		return gb;
	}
}
