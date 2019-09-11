package com.lzkj.mobile.client;

import java.util.List;
import java.util.Map;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.lzkj.mobile.vo.AgentAccVO;
import com.lzkj.mobile.vo.AgentInfoVO;
import com.lzkj.mobile.vo.AgentIsIosVO;
import com.lzkj.mobile.vo.AgentSystemStatusInfoVO;
import com.lzkj.mobile.vo.BankCardTypeVO;
import com.lzkj.mobile.vo.CloudShieldConfigurationVO;
import com.lzkj.mobile.vo.DayRankingRewardVO;
import com.lzkj.mobile.vo.MyPlayerVO;
import com.lzkj.mobile.vo.MyPopularizeVO;
import com.lzkj.mobile.vo.MyQmTxRecord;
import com.lzkj.mobile.vo.MyRewardRecordVO;
import com.lzkj.mobile.vo.MyRewardVO;
import com.lzkj.mobile.vo.QmAchievementVO;
import com.lzkj.mobile.vo.QmDayPromotionDetailVO;
import com.lzkj.mobile.vo.QmLiquidationPageVo;
import com.lzkj.mobile.vo.QmPromotionListVO;
import com.lzkj.mobile.vo.QmUserRewardVO;
import com.lzkj.mobile.vo.QmWeekTopListVO;
import com.lzkj.mobile.vo.ZzSysRatioVO;

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

	@RequestMapping("/agent/mobile/promotion/getDayPromotion")
    QmDayPromotionDetailVO getDayPromotion(@RequestParam("userId") Integer userId);

    @RequestMapping("/agent/mobile/promotion/getUserReward")
    QmUserRewardVO getUserReward(@RequestParam("userId") Integer userId);

    @RequestMapping("/agent/mobile/promotion/getPromotionDetails")
    List<QmPromotionListVO> getPromotionDetails(@RequestParam("userId") Integer userId);

    @RequestMapping("/agent/mobile/promotion/getAppLiquidation")
    QmLiquidationPageVo getAppLiquidation(@RequestParam("userId") Integer userId, @RequestParam("pageIndex") int pageIndex, @RequestParam("pageSize") int pageNumber);

    @RequestMapping("/agent/mobile/promotion/getWeekTopList")
    List<QmWeekTopListVO> getWeekTopList();

    /******************************代理系统**************************************/

    @RequestMapping("/agent/mobile/agentsystem/getAgentMyPopularize")
    MyPopularizeVO getAgentMyPopularize(@RequestParam("userId") Integer userId);

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
    List<ZzSysRatioVO> getZzSysRatio();


    @RequestMapping("/agent/mobile/agentsystem/getAgentMyTeam")
    List<MyPlayerVO> getAgentMyTeam(@RequestParam("userId") Integer userId, @RequestParam("agentId") Integer agentId,
                                    @RequestParam("pageSize") Integer pageSize, @RequestParam("pageIndex") Integer pageIndex);

    /**\
     * 验证电话号是否存在黑名单
     */
    @RequestMapping("/agent/mobile/getPhoneCount")
    Integer getPhoneCount(@RequestParam("mobilePhone") String mobilePhone,@RequestParam("agentId") Integer agentId);

    @RequestMapping("/agent/mobile/getBindMobileSendInfo")
    List<AgentSystemStatusInfoVO> getBindMobileSendInfo(@RequestParam("agentId") Integer agentId);

    @RequestMapping("/agent/mobile/isOpenVerification")
    Integer isOpenVerification( @RequestParam("agentId")Integer agentId, @RequestParam("statusValue")String statusValue);

    @RequestMapping("/agent/mobile/getAgentHotVersion")
    String getAgentHotVersion(@RequestParam("agentId")Integer agentId);
    //查询银行卡类型
  	@RequestMapping("/agent/mobile/getBankCardTypeInfo")
  	List<BankCardTypeVO> getBankCardTypeInfo(@RequestParam("agentId") Integer agentId);
  	
  	@RequestMapping("/agent/mobile/getTomorrowRankingList")
  	List<DayRankingRewardVO> getTomorrowRankingList(@RequestParam("parentId")Integer parentId);
  	
  	@RequestMapping("/agent/mobile/getMyTomorrowRanking")
  	DayRankingRewardVO getMyTomorrowRanking(@RequestParam("userId")Integer userId);
  	
  	@RequestMapping("/agent/mobile/receiveReward")
    Map<String, Object> receiveReward(@RequestParam("id")Integer id);
  	
  	//查询云盾配置
    @RequestMapping("/agentControl/getCloudShieldConfigurationInfos")
    List<CloudShieldConfigurationVO> getCloudShieldConfigurationInfos(@RequestParam("agentId")Integer agentId);
}
