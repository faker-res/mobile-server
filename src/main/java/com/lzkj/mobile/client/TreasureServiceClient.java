package com.lzkj.mobile.client;

import com.lzkj.mobile.vo.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author xxx
 */
@FeignClient(name = "treasure-service")
public interface TreasureServiceClient {

	@RequestMapping("treasure/mobile/getUserGameScoreInfoList")
	List<UserGameScoreInfoVO> getUserGameScoreInfoList(@RequestParam("userId") Integer userId);

	@RequestMapping("treasure/mobile/getScoreRankList")
	ScoreRankPageVO getScoreRankList(@RequestParam("pageIndex") int pageIndex, @RequestParam("pageSize") int pageSize);

	@RequestMapping("treasure/mobile/getLotteryConfig")
	 LotteryConfigVO getLotteryConfig();

	@RequestMapping("treasure/mobile/getGlobalSpreadInfo")
    GlobalSpreadInfo getGlobalSpreadInfo();

	@RequestMapping("treasure/mobile/getInsureTradeRecord")
	RecordInsurePageVO getInsureTradeRecord(@RequestParam("pageIndex") int pageIndex, @RequestParam("pageSize") int pageSize, @RequestParam("userId") Integer userId);

    /**
     * 获取支付商列表(第三方)
     *
     * @param userId
     * @param agentId
     * @return
     */
    @RequestMapping("treasure/mobile/getPayList")
    Map<String, List<PayInfoVO>> getPayList(@RequestParam("userId") Integer userId, @RequestParam("agentId") Integer agentId);

    @RequestMapping("treasure/mobile/getGameRecord")
    Object getGameRecord(@RequestParam("pageIndex") int pageIndex, @RequestParam("pageSize") int pageSize, @RequestParam("userId") Integer userId, @RequestParam("kindId") Integer kindId);

    /**
     * 支付跳转 获取渠道数据
     *
     * @param qudaoId
     * @return
     */
    @RequestMapping("treasure/mobile/getPayInfo")
    ViewPayInfoVO getPayInfo(@RequestParam("qudaoId") int qudaoId);

    /**
     * 获取支付Key
     */
    @RequestMapping("treasure/mobile/getPayOwnerInfo")
    TpayOwnerInfoVO getPayOwnerInfo();

    /**
     * 保存充值订单记录
     *
     * @param onLineOrderVO
     */
    @RequestMapping("treasure/mobile/getRequestOrder")
    HashMap getRequestOrder(@RequestBody OnLineOrderVO onLineOrderVO);

    /**
     * 获取网关信息
     */
    @RequestMapping("treasure/mobile/getGatewayInfo")
    GatewayInfo getGatewayInfo(@RequestParam("account") String account, @RequestParam("passWord") String passWord, @RequestParam("type") Integer type, @RequestParam("agentId") Integer agentId);


    /**
     * 支付回调成功后插入记录
     *
     * @param shareDetailInfoVO
     * @return
     */
    @RequestMapping("treasure/mobile/filliedOnline")
    Map<String, Object> filliedOnline(@RequestBody ShareDetailInfoVO shareDetailInfoVO);

    /**
     * 查询公司支付通道
     *
     * @param agentId
     * @return
     */
    @RequestMapping("treasure/mobile/getCompanyPay")
    List<CompanyPayVO> getCompanyPay(@RequestParam("userId") Integer userId, @RequestParam("agentId") Integer agentId);

    /**
     * 获取公司支付类型
     */
    @RequestMapping("treasure/mobile/getCompanyPayType")
    List<CompanyPayVO> getCompanyPayType(@RequestParam("agentId") Integer agentId, @RequestParam("payType") String payType);

    /**
     * 获取支付记录
     */
    @RequestMapping("treasure/mobile/getRecordList")
    List<AgentCompanyRecordVO> getRecordList(@RequestParam("userId") Integer userId);

	/**
	 * 公司支付 判断使用过程中通道是否被关闭了
	 * @param payId
	 * @return
	 */
	@RequestMapping("treasure/mobile/checkPayIdIsOpen")
	Integer checkPayIdIsOpen(@RequestParam("agentId") Integer agentId,@RequestParam("payId") Integer payId);

	/**
	 * 生成公司充值订单
	 */
	@RequestMapping("treasure/mobile/insertRecord")
	Map insertRecord(@RequestParam("agentId") Integer agentId, @RequestParam("userId") Integer userId, @RequestParam("gameId") Integer gameId,
                     @RequestParam("payId") Integer payId, @RequestParam("orderAmount") BigDecimal orderAmount, @RequestParam("remarks") String remarks, @RequestParam("account") String account);

    @RequestMapping("treasure/mobile/getPayId")
    Integer getPayId(@RequestParam("agentId") Integer agentId, @RequestParam("payName") String payName);

    @RequestMapping("treasure/mobile/getUpdateAddress")
    GatewayInfo getUpdateAddress(@RequestParam("agentId") Integer agentId);

    @RequestMapping("treasure/mobile/updateMerchantOrderId")
    void updateMerchantOrderId(@RequestParam("ownerOrderId") String ownerOrderId, @RequestParam("merchantOrderId") String merchantOrderId, @RequestParam("orderStatus") Integer orderStatus);

    @RequestMapping("treasure/mobile/updatePassagewayResponse")
    void updatePassagewayResponse(@RequestParam("ownerOrderId") String ownerOrderId, @RequestParam("passagewayResponse") String passagewayResponse);

    //余额宝是否开启
    @RequestMapping("treasure/mobile/getYebIsOpen")
    YebConfigVO getYebIsOpen(@RequestParam("agentId") Integer agentId);

    //余额宝收益明细
    @RequestMapping("treasure/mobile/getyebProfitDetailsList")
    List<yebProfitDetailsVO> getyebProfitDetailsList(@RequestParam("userId") Integer userId, @RequestParam("pageIndex") Integer pageIndex, @RequestParam("pageSize") Integer pageSize);

    //收益总数
    @RequestMapping("treasure/mobile/getYwbAfterInfo")
    yebProfitDetailsVO getYwbAfterInfo(@RequestParam("userId") Integer userId);

    @RequestMapping("/treasure/mobile/getUserGameScore")
    Map<String, BigDecimal> getUserGameScore(@RequestParam("userId") Integer userId);

    @RequestMapping("/treasure/mobile/getWeekRankList")
    List<DayUserAbsScoreVO> getWeekRankList(@RequestParam("parentId") Integer parentId);

    @RequestMapping("/treasure/mobile/getLastWeekRank")
    List<DayUserAbsScoreVO> getLastWeekRank(@RequestParam("parentId") Integer parentId);

    @RequestMapping("/treasure/mobile/getLucky")
    LuckyTurntableConfigurationVO getLucky(@RequestParam("agentId") Integer agentId);


    @RequestMapping("treasure/mobile/getLuckyIsOpen")
    LuckyTurntableConfigurationVO getLuckyIsOpen(@RequestParam("parentId") Integer parentId);

    @RequestMapping("treasure/mobile/getVideoType")
    List<VideoTypeVO> getVideoType();

    @RequestMapping("treasure/mobile/getTransactionType")
    List<TransactionTypeVO> getTransactionType();

//	@RequestMapping("treasure/mobile/getMemberRecharge")
//	List<MemberRechargeVO> getMemberRecharge(@RequestParam("userId")Integer userId);
//
//	@RequestMapping("treasure/mobile/getMemberWithdrawal")
//	List<MemberRechargeVO> getMemberWithdrawal(@RequestParam("userId")Integer userId);
//
//	@RequestMapping("treasure/mobile/getCheckInGift")
//	List<MemberRechargeVO> getCheckInGift(@RequestParam("userId")Integer userId);
//
//	@RequestMapping("treasure/mobile/getTaskReward")
//	List<MemberRechargeVO> getTaskReward(@RequestParam("userId")Integer userId);
//
//	@RequestMapping("treasure/mobile/getDailyRecharge")
//	List<MemberRechargeVO> getDailyRecharge(@RequestParam("userId")Integer userId);
//
//	@RequestMapping("treasure/mobile/getRebateReward")
//	List<MemberRechargeVO> getRebateReward(@RequestParam("userId")Integer userId);
//
//	@RequestMapping("treasure/mobile/getVipReward")
//	List<MemberRechargeVO> getVipReward(@RequestParam("userId")Integer userId);

    /**
     * 账户明细
     *
     * @param userId
     * @param typeId
     * @param date
     * @param pageSize
     * @param pageIndex
     * @return
     */
    @RequestMapping("/treasure/mobile/getAccountDetails")
    CommonPageVO<MemberRechargeVO> getAccountDetails(@RequestParam("userId") Integer userId,
                                                     @RequestParam("typeId") Integer typeId,
                                                     @RequestParam("date") Integer date,
                                                     @RequestParam("pageSize") Integer pageSize,
                                                     @RequestParam("pageIndex") Integer pageIndex);

    @RequestMapping("treasure/mobile/accountChangeStatistics")
    AccountChangeStatisticsVO accountChangeStatistics(@RequestParam("userId") Integer userId,
                                                      @RequestParam("date") Integer date);

    @RequestMapping("/treasure/mobile/verifyPassword")
    String verifyPassword(@RequestParam("userId") Integer userId);

    @RequestMapping("/treasure/mobile/getPayLineConfig")
    List<PayUrlListVO> getPayLineConfig();

    @RequestMapping("/treasure/mobile/updatePayUrl")
    void updatePayUrl(@RequestParam("url") String url);


    @RequestMapping("/treasure/mobile/updateDomainRecord")
    void updateDomainRecord(@RequestParam("status") Integer status, @RequestParam("date") String date, @RequestParam("sort") Integer sort);


    @RequestMapping("/treasure/mobile/getYebScore")
    YebScoreVO getYebScore(@RequestParam("userId") Integer userId);

    @RequestMapping("/treasure/mobile/getUserRewardDetail")
    CommonPageVO<UserRewardDetailVO> getUserRewardDetail(@RequestParam("userId") Integer userId, @RequestParam("parentId") Integer parentId, @RequestParam("pageSize") Integer pageSize, @RequestParam("pageIndex") Integer pageIndex);

    @RequestMapping("/treasure/mobile/getUserRecordInsure")
    CommonPageVO<UserRecordInsureVO> getUserRecordInsure(@RequestParam("userId") Integer userId, @RequestParam("date") Integer date, @RequestParam("pageSize") Integer pageSize, @RequestParam("pageIndex") Integer pageIndex, @RequestParam("typeId") Integer typeId);

    //@RequestMapping("/treasure/mobile/getUserYebIncome")

    //CommonPageVO<UserYebIncomeVO> getUserYebIncome(@RequestParam("userId") Integer userId,@RequestParam("date") Integer date,@RequestParam("pageSize")Integer pageSize,@RequestParam("pageIndex")Integer pageIndex);

    @RequestMapping("/treasure/mobile/getLQRecord")
    int getLQRecord(@RequestParam("userId") Integer userId);

    @RequestMapping("/treasure/mobile/getUserYebIncome")
    CommonPageVO<UserRecordInsureVO> getUserYebIncome(@RequestParam("userId") Integer userId, @RequestParam("date") Integer date, @RequestParam("pageSize") Integer pageSize, @RequestParam("pageIndex") Integer pageIndex);

	/*@RequestMapping("/agentControl/getIndividualDatumStatus")
	Boolean getIndividualDatumStatus(@RequestParam("agentId")Integer agentId,@RequestParam("gameId") Integer gameId);*/

    @RequestMapping("/agentControl/IndividualDatum")
    CommonPageVO<IndividualDatumVO> IndividualDatum(@RequestParam("agentId") Integer agentId, @RequestParam("gameId") Integer gameId, @RequestParam("startDate") String startDate, @RequestParam("endDate") String endDate, @RequestParam("status") Integer status, @RequestParam("pageIndex") Integer pageIndex, @RequestParam("pageSize") Integer pageSize);

    @RequestMapping("/agentControl/getIndividualDatum")
    IndividualDatumVO getIndividualDatum(@RequestParam("agentId") Integer agentId, @RequestParam("gameId") Integer gameId);


    @RequestMapping("/treasure/mobile/getRebateInfo")
    AgentRebateConfigVO getRebateInfo(@RequestParam("agentId") Integer agentId, @RequestParam("userId") Integer userId);

    // -------------------幸运注单 start-----------------------
	/*@RequestMapping("/agentControl/getLuckyOrderConfigList")
	CommonPageVO<LuckyOrderConfigVO> getLuckyOrderConfigList(
			@RequestParam("pageIndex")Integer pageIndex,@RequestParam("pageSize")Integer pageSize,
			@RequestParam("kindId")Integer kindId,@RequestParam("kindType")Integer kindType,
			@RequestParam("betAmount")BigDecimal betAmount,@RequestParam("joinTimeLimit")Integer joinTimeLimit,
			@RequestParam("numberMatchType")Integer numberMatchType,@RequestParam("awardType")Integer awardType,
			@RequestParam("agentId")Integer agentId);*/
    @RequestMapping("/agentControl/getLuckyOrderConfig")
    LuckyOrderConfigVO getLuckyOrderConfig(@RequestParam("agentId") Integer agentId);

    /**
     * 查询幸运注单
     *
     * @param pageIndex
     * @param pageSize
     * @param prizeState
     * @param applyState
     * @param agentId
     * @param userId
     * @param startDate
     * @param endDate
     * @param kindId
     * @param kindType
     * @param gameId
     * @return
     */
    @RequestMapping("/agentControl/getLuckyOrderInfoList")
    CommonPageVO<LuckyOrderInfoVO> getLuckyOrderInfoList(
            @RequestParam("pageIndex") Integer pageIndex, @RequestParam("pageSize") Integer pageSize,
            @RequestParam("prizeState") Integer prizeState, @RequestParam("applyState") Integer applyState,
            @RequestParam("agentId") Integer agentId, @RequestParam("userId") Integer userId,
            @RequestParam("startDate") String startDate, @RequestParam("endDate") String endDate,
            @RequestParam("kindId") Integer kindId, @RequestParam("kindType") Integer kindType, @RequestParam("gameId") Integer gameId);

    /**
     * 幸运注单明细
     *
     * @param agentId
     * @param userId
     * @param pageIndex
     * @param pageSize
     * @return
     */
    @RequestMapping("/agentControl/luckyOrderDetailList")
    CommonPageVO<LuckyOrderDetailVo> luckyOrderDetailList(@RequestParam("agentId") Integer agentId,
                                                          @RequestParam("userId") Integer userId,
                                                          @RequestParam("pageIndex") Integer pageIndex,
                                                          @RequestParam("pageSize") Integer pageSize);

    @RequestMapping("/agentControl/receiveLuckyOrderInfo")
    Boolean receiveLuckyOrderInfo(@RequestBody LuckyOrderInfoVO vo);

    /**
     * 手动申请幸运注单奖励
     *
     * @param vo
     * @return
     */
    @RequestMapping("/agentControl/applyLuckyOrderInfo")
    Boolean applyLuckyOrderInfo(@RequestBody LuckyOrderInfoVO vo);

    // -------------------幸运注单 end-----------------------
    @RequestMapping("/treasure/mobile/winAndLose")
    TodayWinOrLoseVO winAndLose(@RequestParam("userId") Integer userId, @RequestParam("beginTime") String beginTime, @RequestParam("endTime") String endTime);

    // ------------------------第三方APP支付 start--------------------------------

    /**
     * 第三方APP支付
     *
     * @param agentId
     * @param userId
     * @return
     */
    @RequestMapping("/treasure/mobile/getThirdAppPayConfigList")
    List<ThirdAppPayConfigVO> getThirdAppPayConfigList(@RequestParam("agentId") Integer agentId,
                                                       @RequestParam("userId") Integer userId);

    /**
     * 新增第三方APP支付记录
     *
     * @param vo
     * @return
     */
    @RequestMapping("/treasure/mobile/insertThirdPayRecord")
    Boolean insertThirdPayRecord(@RequestBody ThirdAppPayRecordVO vo);

    // ------------------------第三方APP支付 end--------------------------------

    // ---------------------   负盈利新增 ----------------

    @RequestMapping("/treasure/mobile/getMoney")
    SelfMoneyVO getMoney(@RequestParam("agentId") Integer agentId, @RequestParam("userId") Integer userId);

    @RequestMapping("/treasure/mobile/getMyRebate")
    ProgramVO getMyRebate(@RequestParam("agentId") Integer agentId, @RequestParam("userId") Integer userId);

    @RequestMapping("/treasure/mobile/applicationRebate")
    ApplicationVO applicationRebate(@RequestParam("agentId") Integer agentId, @RequestParam("userId") Integer userId);

    @RequestMapping("/treasure/mobile/submitApplication")
    Boolean submitApplication(@RequestParam("agentId") Integer agentId, @RequestParam("userId") Integer userId, @RequestParam("gameId") Integer gameId);

    @RequestMapping("/treasure/mobile/getRebateTutorial")
    String getRebateTutorial(@RequestParam("agentId") Integer agentId);

    @RequestMapping("/treasure/mobile/getMyTeam")
    CommonPageVO<MyTeamVO> getMyTeam(@RequestParam("agentId") Integer agentId, @RequestParam("userId") Integer userId,
                             @RequestParam("pageIndex") Integer pageIndex, @RequestParam("pageSize") Integer pageSize,
                             @RequestParam("gameId") Integer gameId, @RequestParam("dateTime") String dateTime, @RequestParam("nullity")Integer nullity);

    @RequestMapping("/treasure/mobile/getMyTeamCount")
    Integer getMyTeamCount(@RequestParam("agentId") Integer agentId, @RequestParam("userId") Integer userId,
                           @RequestParam("gameId") Integer gameId, @RequestParam("dateTime") String dateTime);

    @RequestMapping("/treasure/mobile/getMyTeamTodayBet")
    Integer getMyTeamTodayBet(@RequestParam("userId") Integer userId);

    @RequestMapping("/treasure/mobile/getMyTeamOrder")
    CommonPageVO<MyTeamVO> getMyTeamOrder(@RequestParam("agentId") Integer agentId, @RequestParam("gameId") Integer gameId, @RequestParam("userId") Integer userId,
                                        @RequestParam("pageIndex") Integer pageIndex, @RequestParam("pageSize") Integer pageSize,
                                        @RequestParam("startTime") String startTime, @RequestParam("endTime") String endTime);

    @RequestMapping("/treasure/mobile/getMyTeamBeat")
    CommonPageVO<MyTeamVO> getMyTeamBeat(@RequestParam("agentId") Integer agentId, @RequestParam("userId") Integer userId,
                                       @RequestParam("pageIndex") Integer pageIndex, @RequestParam("pageSize") Integer pageSize,
                                       @RequestParam("startTime") String startTime, @RequestParam("endTime") String endTime);

    @RequestMapping("/treasure/mobile/getSelfReport")
    SelfReportVO getSelfReport(@RequestParam("agentId")Integer agentId, @RequestParam("userId")Integer userId,
                                 @RequestParam("startTime")String startTime, @RequestParam("endTime")String endTime);

    @RequestMapping("/treasure/mobile/submitDomain")
    Boolean submitDomain(@RequestParam("agentId")Integer agentId, @RequestParam("userId")Integer userId, @RequestParam("domain")String domain,
                         @RequestParam("duration")Integer duration, @RequestParam("cost")String cost,@RequestParam("gameId")Integer gameId);

    @RequestMapping("/treasure/mobile/getDomainFee")
    List<DomainVO> getDomainFee(@RequestParam("agentId")Integer agentId);

    @RequestMapping("/treasure/mobile/getDomain")
    List<LinkVO> getDomain(@RequestParam("agentId")Integer agentId,@RequestParam("userId")Integer userId);

    @RequestMapping("/treasure/mobile/getRebate")
    List<RebateInfoVO> getRebate(@RequestParam("agentId")Integer agentId,@RequestParam("userId")Integer userId);

    @RequestMapping("/treasure/mobile/getRebateByTime")
    RebateInfoVO getRebateByTime(@RequestParam("agentId")Integer agentId, @RequestParam("userId")Integer userId,
                                       @RequestParam("startTime")String startTime, @RequestParam("endTime")String endTime);
}

