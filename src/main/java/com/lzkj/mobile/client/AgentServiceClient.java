package com.lzkj.mobile.client;

import com.lzkj.mobile.v2.common.Response;
import com.lzkj.mobile.v2.inputVO.activity.ReceivingRedEnvelopeRainVO;
import com.lzkj.mobile.vo.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@FeignClient(name = "agent-service")
public interface AgentServiceClient {

	@RequestMapping("agent/mobile/getAppStore")
	List<AgentIsIosVO> getAppStore(@RequestParam("agentAcc") String agentAcc);

	@RequestMapping("agent/mobile/getAppStoreById")
	List<AgentIsIosVO> getAppStoreById(@RequestParam("agentId") Integer agentId);
	@RequestMapping("agent/mobile/getAgentPay")
    List<AgentInfoVO> getAgentPay(@RequestParam("agentId") Integer agentId);
	//获取短信验证信息
    @RequestMapping("agent/mobile/getPhoneAgent")
    Integer getPhoneAgent(@RequestParam("agentId") Integer agentId);

    @RequestMapping("agent/mobile/getQrCode")
    AgentAccVO getQrCode(@RequestParam("agentId") Integer agentId);

    @RequestMapping("agent/mobile/getAgentContactInfo")
    AgentAccVO getAgentContactInfo(@RequestParam("agentId") Integer agentId);
    /**********************************************全民代理************************************************/

	@RequestMapping("/agent/mobile/promotion/getDirectPromotionDetail")
    QmDayPromotionDetailVO getDirectPromotionDetail(@RequestParam("userId") Integer userId);

    @RequestMapping("/agent/mobile/promotion/getUserReward")
    QmUserRewardVO getUserReward(@RequestParam("userId") Integer userId);


    @RequestMapping("/agent/mobile/promotion/getAppLiquidation")
    QmLiquidationPageVo getAppLiquidation(@RequestParam("userId") Integer userId, @RequestParam("pageIndex") int pageIndex, @RequestParam("pageSize") int pageNumber);

    @RequestMapping("/agent/mobile/promotion/getDirectAchieve")
    List<QmPromotionDetailVO> getDirectAchieve(@RequestParam("userId") Integer userId,@RequestParam("gameId") Integer gameId);

    @RequestMapping("/agent/mobile/promotion/getZzSysConfig")
    List<ZzSysRatioVO> getZzSysConfig(@RequestParam("agentId") Integer agentId);

    @RequestMapping("/agent/mobile/promotion/getDirectQuery")
    List<QmDirectQueryVO> getDirectQuery(@RequestParam("userId") Integer userId,
            @RequestParam("gameId") Integer gameId,@RequestParam("date") String date,@RequestParam("page") int page);


    @RequestMapping("/agent/mobile/promotion/getAchieveDetail")
    List<QmPromotionDetailVO> getAchieveDetail(@RequestParam("userId")Integer userId, @RequestParam("kindType")Integer kindType);

    @RequestMapping("/agent/mobile/promotion/receiveCommission")
    BigDecimal receiveCommission(@RequestParam("userId")Integer userId);

    @RequestMapping("/agent/mobile/promotion/getTeamMember")
    MyPopularizeVO getTeamMember(@RequestParam("userId")Integer userId);

    @RequestMapping("/agent/mobile/agentsystem/getAgentMyPopularize")
    MyPopularizeVO getAgentMyPopularize( @RequestParam("userId") Integer userId);
    /******************************代理系统**************************************/


    @RequestMapping("/agent/mobile/agentsystem/getAgentMyPlayer")
    List<MyPlayerVO> getAgentMyPlayer(@RequestParam("userId") Integer userId, @RequestParam("memberId") Integer memberId, @RequestParam("pageIndex") Integer pageIndex);


    @RequestMapping("/agent/mobile/agentsystem/achievement")
    List<QmAchievementVO> getAchievement(@RequestParam("userId") Integer userId);

    @RequestMapping("/agent/mobile/agentsystem/getMyRewardRecord")
    List<MyRewardRecordVO> getMyRewardRecord(@RequestParam("userId") Integer userId);

    @RequestMapping("/agent/mobile/agentsystem/getMyQmTxRecord")
    List<MyQmTxRecord> getMyQmTxRecord(@RequestParam("userId") Integer userId, @RequestParam("pageSize") Integer pageSize, @RequestParam("pageIndex") Integer pageIndex);

    @RequestMapping("/agent/mobile/agentsystem/getMyReward")
    MyRewardVO getMyReward(@RequestParam("userId") Integer userId);

    @RequestMapping("/agent/mobile/agentsystem/getZzSysRatio")
    List<ZzSysRatioVO> getZzSysRatio(@RequestParam("agentId") Integer agentId);


    @RequestMapping("/agent/mobile/agentsystem/getAgentMyTeam")
    List<MyPlayerVO> getAgentMyTeam(@RequestParam("userId") Integer userId, @RequestParam("agentId") Integer agentId,
                                    @RequestParam("pageSize") Integer pageSize, @RequestParam("pageIndex") Integer pageIndex);

    /**
     * 验证电话号是否存在黑名单
     *
     * @param mobilePhone
     * @param agentId
     * @return
     */
    @RequestMapping("/agent/mobile/getPhoneCount")
    Integer getPhoneCount(@RequestParam("mobilePhone") String mobilePhone,
                          @RequestParam("agentId") Integer agentId);

    /**
     * 校验该手机号是否已经在平台注册过
     *
     * @param phone
     * @param agentId
     * @return
     */
    @RequestMapping("/agent/mobile/isAlreadyRegister")
    boolean isAlreadyRegister(@RequestParam("phone") String phone,
                              @RequestParam("agentId") Integer agentId);

    @RequestMapping("/agent/mobile/getBindMobileSendInfo")
    List<AgentSystemStatusInfoVO> getBindMobileSendInfo(@RequestParam("agentId") Integer agentId);

    @RequestMapping("/agent/mobile/isOpenVerification")
    Integer isOpenVerification( @RequestParam("agentId")Integer agentId, @RequestParam("statusValue")String statusValue);

    @RequestMapping("/agent/mobile/getAgentHotVersion")
    String getAgentHotVersion(@RequestParam("agentId")Integer agentId);
    //查询银行卡类型
  	@RequestMapping("/agent/mobile/getBankCardTypeInfo")
  	List<BankCardTypeVO> getBankCardTypeInfo(@RequestParam("agentId") Integer agentId);

  	@RequestMapping("/agent/mobile/getLastRankingList")
  	List<WeekRankingListVO> getLastRankingList(@RequestParam("parentId")Integer parentId);

  	@RequestMapping("/agent/mobile/receiveReward")
    Map<String, Object> receiveReward(@RequestParam("id")Integer id);

  	//查询云盾配置
    @RequestMapping("/agent/mobile/getCloudShieldConfigurationInfos")
    List<CloudShieldConfigurationVO> getCloudShieldConfigurationInfos(@RequestParam("agentId")Integer agentId);

    @RequestMapping("/agent/mobile/getPlayerRankInfo")
    List<WeekRankingListVO> getPlayerRankInfo(@RequestParam("userId")Integer userId);

    @RequestMapping("/agent/mobile/getAgentRankList")
    List<AgencyEqualReward> getAgentRankList(@RequestParam("agentId")Integer agentId);

    @RequestMapping("agent/access2game/getAccessAgent")
    AgentAccVO getAccessAgent(@RequestParam("agent") Integer agent);

    @RequestMapping("agent/mobile/getUserRebate")
    BigDecimal getUserRebate(@RequestParam("kindType") Integer kindType,@RequestParam("userId") Integer userId,@RequestParam("date") Integer date);

    @RequestMapping("/agent/mobile/fundDetails")
    List<Map<String, Object>> fundDetails( @RequestParam("gameId") Integer gameId, @RequestParam("agentId") Integer agentId);

    @RequestMapping("/agent/mobile/getALLAgent")
    List<Integer> getALLAgent();

    @RequestMapping("/agent/mobile/getRedEnvelopeRain")
    RedEnvelopeRainVO getRedEnvelopeRain(@RequestParam("parentId") Integer parentId,@RequestParam("activityId") Integer activityId);

    @RequestMapping("/agent/mobile/userSingleRedEnvelopeCount")
    Integer userSingleRedEnvelopeCount(@RequestParam("userId") Integer userId, @RequestParam("parentId") Integer parentId, @RequestParam("eventId") Integer eventId);

    @RequestMapping("/agent/mobile/getRedEnvelope")
    RedEnvelopeVO getRedEnvelope(@RequestParam("parentId") Integer parentId);
    
    @RequestMapping("/agent/mobile/getRedEnvelopeSain")
    RedEnvelopeVO getRedEnvelopeSain(@RequestParam("parentId") Integer parentId);
    
    @RequestMapping("/agent/mobile/getTomorrowRedEnvelopeSain")
    RedEnvelopeVO getTomorrowRedEnvelopeSain(@RequestParam("parentId") Integer parentId);
    
    @RequestMapping("/agent/mobile/getCurrentDate")
    Long getCurrentDate();

    @RequestMapping("/mobile/activity/receiveRedEnvelopeRain")
    Response<Map<String, Object>> receiveRedEnvelopeRain(@RequestBody ReceivingRedEnvelopeRainVO vo);
    
    @RequestMapping("/agent/mobile/getUserRankings")
    List<UserRankinsVO> getUserRankings(@RequestParam("parentId") Integer parentId);
    
    @RequestMapping("/agent/mobile/getRedEnvelopeType")
    List<RedEnvelopeConditionTypeVO> getRedEnvelopeType();
    
    @RequestMapping("/agent/mobile/getRedEnvelopeRecord")
    CommonPageVO<RedEnvelopeRecordVO> getRedEnvelopeRecord(@RequestParam("userId") Integer userId,@RequestParam("typeId") Integer typeId,@RequestParam("pageIndex") Integer pageIndex,@RequestParam("pageSize") Integer pageSize,@RequestParam("date") Integer date);
    
    @RequestMapping("/agent/mobile/getRedEnvepoleYuStartTimeAndEndTime")
    RedEnvepoleYuStartTimeAndEndTimeVO getRedEnvepoleYuStartTimeAndEndTime(@RequestParam("parentId") Integer parentId,@RequestParam("eventId") Integer eventId);

    @RequestMapping("/agent/mobile/hasFreeRedEnvelope")
    Integer todayRedEnvelopeRainCount(@RequestParam("eventId") Integer eventId, @RequestParam("parentId") Integer parentId);
}
