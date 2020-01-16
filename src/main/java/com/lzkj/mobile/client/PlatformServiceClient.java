package com.lzkj.mobile.client;

import com.lzkj.mobile.vo.*;

import javafx.beans.binding.BooleanBinding;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
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
	Map<String, Object> getVipConfig(@RequestParam("parentId")Integer parentId, @RequestParam("userId") Integer userId);

	@RequestMapping("/platform/mobile/levelReward")
	String levelReward(@RequestParam("userId")Integer userId, @RequestParam("level")Integer level,@RequestParam("reward") BigDecimal reward);

	@RequestMapping("/platform/mobile/pointReward")
	String pointReward(@RequestParam("userId")Integer userId, @RequestParam("type")Integer type);

	//查询代理客服信息
	@RequestMapping("/platform/mobile/getAgentCustomerServiceInfo")
	List<CustomerServiceConfigVO> getAgentCustomerServiceInfo(@RequestParam("agentId") Integer agentId);

	@RequestMapping("/platform/mobile/getServerName")
	Map<String,Object> getServerName(@RequestParam("serverId") Integer serverId);

	@RequestMapping("/platform/mobile/getMobileThirdKindList")
	List<ThirdKindConfigVO> getMobileThirdKindList(@RequestParam("agentId")Integer agentId);


	@RequestMapping("/platform/mobile/getUserVIPLevelReward")
	List<VipLevelRewardVO> getUserVIPLevelReward(@RequestParam("parentId")Integer parentId);

	@RequestMapping("/platform/mobile/getUserWeekReceive")
	List<VIPReceiveInfoVO> getUserWeekReceive(@RequestParam("userId")Integer userId,@RequestParam("level")Integer level);

	@RequestMapping("/platform/mobile/getUserMonthReceive")
	List<VIPReceiveInfoVO> getUserMonthReceive(@RequestParam("userId")Integer userId,@RequestParam("level")Integer level);

	@RequestMapping("/platform/mobile/getUserDayReceive")
	List<VIPReceiveInfoVO> getUserDayReceive(@RequestParam("userId")Integer userId,@RequestParam("level")Integer level);

	@RequestMapping("/platform/mobile/getUserYearReceive")
	List<VIPReceiveInfoVO> getUserYearReceive(@RequestParam("userId")Integer userId,@RequestParam("level")Integer level);

	@RequestMapping("/platform/mobile/getUserLevelReceive")
	List<VipRankReceiveVO> getUserLevelReceive(@RequestParam("userId")Integer userId);

	@RequestMapping("/platform/mobile/getVipLevelCount")
	Integer getVipLevelCount(@RequestParam("parentId")Integer parentId);

	@RequestMapping("/platform/mobile/insertVipRankReceive")
	void insertVipRankReceive(@RequestBody List<VipRankReceiveVO> list);


	/***************************************洗码相关***********************************************************/

	@RequestMapping("/mobile/clean/getCleanChips")
	CleanChipsTotalVO getCleanChips(@RequestParam("userId") Integer userId, @RequestParam("agentId") Integer agentId);


	@RequestMapping("/mobile/clean/washBet")
	Map <String ,Object> washBet(@RequestParam("userId") Integer userId, @RequestParam("agentId") Integer agentId,@RequestParam("vipLevel") Integer vipLevel);

	@RequestMapping("/mobile/clean/getWashRecord")
	List<CleanChipsRecordVO> getWashRecord(@RequestParam("userId") Integer userId);
	@RequestMapping("/mobile/clean/getCleanChipsRecord")
	List<CleanChipsRecordVO> getCleanChipsRecord(@RequestParam("userId") Integer userId,@RequestParam("recordTime") String recordTime);

	@RequestMapping("/mobile/clean/getCleanChipsConfig")
    List<CleanChipsConfigVO> getCleanChipsConfig(@RequestParam("agentId") Integer agentId);
	@RequestMapping("/mobile/clean/getThirdConfig")
	List<ThirdKindConfigVO> getThirdConfig(@RequestParam("kindType")int kindType);


	@RequestMapping("/mobile/clean/getSystemOpen")
	List<SystemStatusInfoVO> getSystemOpen(@RequestParam("parentId")Integer parentId);

	@RequestMapping("/platform/mobile/getAgentGameListByGameTypeItem")
    public List<PlatformVO>  getAgentGameListByGameTypeItem(@RequestParam("agentId")Integer agentId);

    @RequestMapping("/platform/mobile/getAgentGameByGameTypeItem")
    public List<AgentMobileKindConfigVO> getAgentGameByGameTypeItem(@RequestParam("agentId")Integer agentId);

    @RequestMapping("/platform/mobile/getYebInterestRate")
    List<YebInterestRateVO> getYebInterestRate(@RequestParam("parentId")Integer parentId);

    @RequestMapping("/platform/mobile/getTaskDetails")
    TaskVO getTaskDetails(@RequestParam("taskId")Integer taskId);
    
    @RequestMapping("/platform/mobile/getTaskinfoByUserId")
    List<AccountsTask> getTaskinfoByUserId(@RequestParam("userId")Integer userId,@RequestParam("agentId")Integer agentId);
    
    @RequestMapping("/platform/mobile/getTaskReward")
    String getTaskReward(@RequestParam("userId")Integer userId,@RequestParam("taskId")Integer taskId,@RequestParam("password")String password,
    		@RequestParam("machinIe")String machinIe,@RequestParam("ip")String ip);
    
    @RequestMapping("/platform/mobile/updateResversion")
    void updateResversion();

	@RequestMapping("/agentSystem/setUp/getUserSignAwardConfigList")
	List<SignAwardConfig> getUserSignAwardConfigList(@RequestParam("agentId") Integer agentId,@RequestParam("userId") Integer userId);
	@RequestMapping("/agentSystem/setUp/acceptUserSignAward")
	BigDecimal acceptUserSignAward(@RequestParam("agentId") Integer agentId,@RequestParam("userId") Integer userId);
}

