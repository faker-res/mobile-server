package com.lzkj.mobile.client;

import com.lzkj.mobile.vo.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@FeignClient(name = "platform-service")
public interface PlatformServiceClient {

	@RequestMapping("/platform/mobile/getMobileKindList")
	List<MobileKind> getMobileKindList(@RequestParam("typeId") Integer typeId, @RequestParam("agentId") Integer agentId);

	@RequestMapping("/platform/mobile/getMobilePropertyType")
	List<GamePropertyType> getMobilePropertyType(@RequestParam("tagId") Integer tagId);

	@RequestMapping("/platform/mobile/getWhitelist")
	int getWhitelist(@RequestParam("registerMachine") String registerMachine);

    @RequestMapping("/platform/mobile/getRebateInfo")
    AgentRebateConfigVO getRebateInfo(@RequestParam("agentId") Integer agentId);

    //查询用户反馈意见
	@RequestMapping("/platform/mobile/getFeedbackInfo")
	List<GameFeedbackVO> getFeedbackInfo(@RequestParam("userId") Integer userId);

	//查询用户反馈意见
	@RequestMapping("/platform/mobile/insertFeedbackInfo")
	Boolean insertFeedbackInfo(@RequestParam("userId") Integer userId, @RequestParam("gameId") Integer gameId, @RequestParam("feedBackType") Integer feedBackType,
                               @RequestParam("feedBackTxt") String feedBackTxt, @RequestParam("serverId") Integer serverId);


	/**
	 * 删除用户反馈信息
	 */
	@RequestMapping("/platform/mobile/deFeedbackInfo")
	Boolean deFeedbackInfo(@RequestParam("id") Integer id);

	//查询客服信息
	@RequestMapping("/platform/mobile/getCustomerInfo")
	List<CustomerServiceConfigVO> getCustomerInfo(@RequestParam("agentId") Integer agentId);
	//查询客服信息
	@RequestMapping("/platform/mobile/getProblemConfigInfo")
	List<ProblemConfigVO> getProblemConfigInfo(@RequestParam("agentId") Integer agentId);

	/**
	 * VIP数据获取
	 */
	//获取游戏用户领取VIP奖励信息
	@RequestMapping("/platform/mobile/getUserReceiveInfo")
	List<VIPReceiveInfoVO> getUserReceiveInfo(@RequestParam("userId") Integer userId);


	@RequestMapping("/platform/mobile/getBankList")
    List<BankInfoVO> getBankList(@RequestParam("agentId") Integer agentId);

	@RequestMapping("/platform/mobile/getVipRankWelfare")
	VipWelfareVO getVipRankWelfare(@RequestParam("agentId")Integer agentId, @RequestParam("gameId") Integer gameId);

	@RequestMapping("/platform/mobile/getVipRelation")
	List<VipIntegrealConfigVO> getVipRelation(@RequestParam("agentId")Integer agentId);

	@RequestMapping("/platform/mobile/getVipConfig")
	Map<String, Object> getVipConfig(@RequestParam("agentId")Integer agentId, @RequestParam("userId") Integer userId);

	@RequestMapping("/platform/mobile/levelReward")
	String levelReward(@RequestParam("userId")Integer userId, @RequestParam("level")Integer level,@RequestParam("reward") BigDecimal reward);

	@RequestMapping("/platform/mobile/pointReward")
	String pointReward(@RequestParam("userId")Integer userId, @RequestParam("type")Integer type);
	
	//查询代理客服信息
	@RequestMapping("/platform/mobile/getAgentCustomerServiceInfo")
	List<CustomerServiceConfigVO> getAgentCustomerServiceInfo(@RequestParam("agentId") Integer agentId);
	
	@RequestMapping("/platform/mobile/getServerName")
	Map<String,Object> getServerName(@RequestParam("serverId") Integer serverId);
	
}
