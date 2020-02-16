package com.lzkj.mobile.client;

import com.lzkj.mobile.entity.AccountsInfoEntity;
import com.lzkj.mobile.entity.InternalMessageEntity;
import com.lzkj.mobile.v2.common.Response;
import com.lzkj.mobile.v2.dto.InternalMessageDto;
import com.lzkj.mobile.v2.returnVO.mail.InternalMessageVO;
import com.lzkj.mobile.vo.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@FeignClient(name = "accounts-service")
public interface AccountsServiceClient {

	@RequestMapping("accounts/mobile/getAccountsInfo")
    AccountsInfoVO getAccountsInfo(@RequestParam("userId") Integer userId);

	@RequestMapping("accounts/mobile/getUserInfoByGameId")
	AccountsInfoVO getUserInfoByGameId(@RequestParam("gameId") Integer gameId);

	@RequestMapping("accounts/mobile/getSystemStatusInfo")
     SystemStatusInfoVO getSystemStatusInfo(@RequestParam("systemConfigKey") String systemConfigKey);

	@RequestMapping("accounts/mobile/getUserInfo")
    UserInfoVO getUserInfo(@RequestParam("userId") int userNo, @RequestParam("gameId") int gameId, @RequestParam("account") String account);

    @RequestMapping("accounts/mobile/getApplyOrder")
    ApplyRecordPageVo getApplyOrder(@RequestParam("userId") Integer userNo, @RequestParam("pageIndex") Integer pageIndex, @RequestParam("pageSize") Integer pageNumber);

    @RequestMapping("accounts/mobile/visitorBind")
    VisitorBindResultVO visitorBind(@RequestBody BindPhoneVO bindPhoneVO);

    //更换手机号码
    @RequestMapping("accounts/mobile/replacePhoneCode")
    VisitorBindResultVO replacePhoneCode(@RequestParam("userId") Integer userId, @RequestParam("phone") String phone);

    @RequestMapping("accounts/mobile/bindOrModifyPayInfo")
    Map<String, Object> bindOrModifyPayInfo(@RequestParam("userId") Integer userId,
                                            @RequestParam("bankRealName") String bankRealName, @RequestParam("bankNo") String bankNo,
                                            @RequestParam("bankName") String bankName, @RequestParam("bankAddress") String bankAddress,
                                            @RequestParam("alipayRealName") String alipayRealName, @RequestParam("alipay") String alipay);

    @RequestMapping("accounts/mobile/updatePassword")
    Map<String, Object> updatePassword(@RequestParam("phone") String phone, @RequestParam("password") String password, @RequestParam("agentId") Integer agentId);

    //修改用户保底佣金
    @RequestMapping("accounts/mobile/editRatio")
    Boolean editRatio(@RequestParam("qmRation") BigDecimal qmRation, @RequestParam("gameId") Integer gameId);

    //查询当前用户上级代理佣金
    @RequestMapping("accounts/mobile/queryParentRation")
    BigDecimal queryParentRation(@RequestParam("gameId") Integer gameId);

    //查询当前用户代理佣金
    @RequestMapping("/accounts/mobile/queryRatioUserInfo")
    BigDecimal  queryRatioUserInfo(@RequestParam("gameId") Integer gameId);

    @RequestMapping("/accounts/mobile/queryRatioUserInfoType")
    BigDecimal queryRatioUserInfoType(@RequestParam("gameId")Integer gameId, @RequestParam("kindType")int kindType);

    //查询当前用户上级代理佣金
    @RequestMapping("accounts/mobile/queryRegisterMobile")
    Integer queryRegisterMobile(@RequestParam("phone") String phone);

    /**
     * 此手机号在同一业主名下是否有注册过
     * @param phone
     * @param agentId
     * @return
     */
    @RequestMapping("accounts/mobile/queryRegisterMobile")
    Integer queryRegisterMobile(@RequestParam("phone") String phone,@RequestParam("agentId") String agentId);

    @RequestMapping("accounts/mobile/getMyDirectlyPlayer")
    Integer getMyDirectlyPlayer(@RequestParam("userId") Integer userId);

    //查询业主最低出售金额
    @RequestMapping("accounts/mobile/getMinBalanceInfo")
    BigDecimal getMinBalanceInfo(@RequestParam("agentId") Integer agentId,@RequestParam("userId")Integer userId);

    //查询用户动态密码
    @RequestMapping("accounts/mobile/getInsurePassInfo")
    String getInsurePassInfo(@RequestParam("userId") Integer userId);

    @RequestMapping("accounts/mobile/updateLoginIp")
    Boolean updateLoginIp(@RequestBody AccReportVO accReportVO);

    //获取前VIP20排行榜功能
    @RequestMapping("/accounts/mobile/getUserVipRankingInfo")
    List<AccountsInfoVO> getUserVipRankingInfo(@RequestParam("agentId") Integer agentId);

    @RequestMapping("/accounts/mobile/getVipRank")
    List<AccountsLevelVO> getVipRank(@RequestParam("agentId")Integer agentId);

    @RequestMapping("/accounts/mobile/getPlayerVipInfo")
    AccountsLevelVO getPlayerVipInfo(@RequestParam("gameId")Integer gameId);

    @RequestMapping("/AgentAccControl/getPlayerLevel")
    AccountsLevelVO getPlayerLevel(@RequestParam("userId")Integer userId);

    /******************  用户邮件接口 start *******************/
    @RequestMapping("/accounts/mobile/getMailsInfo")
    Response<List<InternalMessageVO>> getMailsInfo(@RequestParam("gameId")Integer gameId);

    @RequestMapping("/accounts/mobile/openMail")
    Response<Boolean> openMail(@RequestParam("ids") List<Integer> ids);

    @RequestMapping("/accounts/mobile/openAllMail")
    Response<Boolean> openAllMail(@RequestParam("gameId")Integer gameId);

    @RequestMapping("/accounts/mobile/deleteMail")
    Response<Boolean> deleteMail(@RequestParam("ids") List<Integer> ids);

    @RequestMapping("/accounts/mobile/deleteAllMail")
    Response<Boolean> deleteAllMail(@RequestParam("gameId")Integer gameId);

    @RequestMapping("/accounts/mobile/getOpenMailList")
    Response<List<InternalMessageVO>> getOpenMailList(@RequestParam("ids")List<Integer> ids);

    @RequestMapping("/accounts/mobile/totalMail")
    Response<Integer> totalMail(@RequestParam("gameId")Integer gameId, @RequestParam("agentId")Integer agentId);

    @RequestMapping("/accounts/mobile/isOrNotOpenMail")
    Response<Boolean> isOrNotOpenMail(@RequestParam("agentId")Integer agentId);

    //动态发邮件接口
    @RequestMapping("/accounts/common/sendMail")
    Response<InternalMessageEntity> sendMail(@RequestBody InternalMessageDto dto);
    /******************  用户邮件接口 end *******************/

    @RequestMapping("/accounts/mobile/getCollectionName")
    String getGameItem(@RequestParam("kindId") Integer kindId);

    @RequestMapping("/mobile/set/resetInsurePwd")
    Response<Map<String, Object>> resetInsurePwd(@RequestParam("userId") Integer userId,@RequestParam("oldPwd") String oldPwd,
                                       @RequestParam("newPwd") String newPwd);

    @RequestMapping("/accounts/mobile/userBankInformation")
    int getUserBankInformation(@RequestParam("bankNo") String bankNo);

    @RequestMapping("/accounts/mobile/getUserVipLevel")
    VipLevelRewardVO getUserVipLevel(@RequestParam("userId")Integer userId);

    @RequestMapping("/accounts/mobile/getVipLevelConfig")
    List<VipLevelRewardVO> getVipLevelConfig(@RequestParam("parentId")Integer parentId);

    @RequestMapping("/accounts/common/getUsersInfo")
    AccountsInfoEntity getUsersInfo(@RequestParam("userId")Integer userId);

    @RequestMapping("/accounts/mobile/updateUserBasicInfo")
    int updateUserBasicInfo(@RequestParam("nickName") String nickName,@RequestParam("gender") Integer gender,@RequestParam("userId") Integer userId);

    @RequestMapping("/accounts/mobile/updateUserContactInfo")
    int updateUserContactInfo(@RequestParam("mobilePhone") String mobilePhone,@RequestParam("qq") String qq,@RequestParam("eMail") String eMail,@RequestParam("userId") Integer userId);

    @RequestMapping("/accounts/mobile/getChannelGameUserBetAndScore")
    CommonPageVO<ChannelGameUserBetAndScoreVO> getChannelGameUserBetAndScore(@RequestParam("kindType") Integer kindType,@RequestParam("date") Integer date,@RequestParam("kindId") Integer kindId,@RequestParam("userId") Integer userId,@RequestParam("pageIndex") Integer pageIndex,@RequestParam("pageSize") Integer pageSize);

    @RequestMapping("/accounts/mobile/getPersonalReport")
    PersonalReportVO getPersonalReport(@RequestParam("kindType") Integer kindType,@RequestParam("date") Integer date,@RequestParam("userId") Integer userId);

    @RequestMapping("/accounts/mobile/getUserVipZeroLevel")
    VipLevelRewardVO getUserVipZeroLevel(@RequestParam("userId") Integer userId);

    @RequestMapping("/accounts/mobile/getUserCodeDetails")
    List<UserCodeDetailsVO> cashFlowDetails(@RequestParam("userId") Integer userId, @RequestParam("agentId") Integer agentId);

    @RequestMapping("/accounts/mobile/getRedEnvelopeReward")
    List<ActivityRedEnvelopeRewardVO> getRedEnvelopeReward(@RequestParam("userId") Integer userId,@RequestParam("parentId") Integer parentId);

    @RequestMapping("/accounts/mobile/getReceivingRedEnvelope")
    Integer getReceivingRedEnvelope(@RequestParam("userId") Integer userId,@RequestParam("score") BigDecimal score,@RequestParam("ip") String ip,@RequestParam("machineId") String machineId,@RequestParam("typeId") Integer typeId,@RequestParam("activityId") Integer activityId);

    @RequestMapping("/accounts/mobile/verifyPassword")
    String verifyPassword(@RequestParam("userId") Integer userId);

    @RequestMapping("/accounts/mobile/getRedEnvelope")
    List<ActivityRedEnvelopeVO> getRedEnvelope(@RequestParam("userId") Integer userId,@RequestParam("parentId") Integer parentId);


    @RequestMapping("/accounts/mobile/getRedEnvepoleRules")
    RedEnvepoleRulesVO getRedEnvepoleRules(@RequestParam("parentId") Integer parentId);

    @RequestMapping("/accounts/mobile/getReceiveRedEnvelopeRecord")
    Integer getReceiveRedEnvelopeRecord(@RequestParam("userId") Integer userId);
    
    @RequestMapping("/accounts/mobile/getUserLoginRedEnvelopeIsOpen")
    Integer getUserLoginRedEnvelopeIsOpen(@RequestParam("parentId") Integer parentId);
    
    @RequestMapping("/accounts/mobile/getLoginRedEnvelopeAmoutByParentId")
    BigDecimal getLoginRedEnvelopeAmoutByParentId(@RequestParam("parentId") Integer parentId);
    
    @RequestMapping("/accounts/mobile/getUserLoginRedEnvelope")
    Integer getUserLoginRedEnvelope(@RequestParam("parentId") Integer parentId);

    @RequestMapping("/accounts/mobile/getUserRedEnvelopeRain")
    Integer getUserRedEnvelopeRain(@RequestParam("parentId") Integer parentId);

    @RequestMapping("/accounts/mobile/saveBankCardRawData")
    Boolean saveBankCardRawData(@RequestParam("userId")Integer userId,
                                @RequestParam("gameId")Integer gameId,
                                @RequestParam("agentId")Integer agentId,
                                @RequestParam("bankNo")String bankNo,
                                @RequestParam("bankName")String bankName,
                                @RequestParam("compellation")String compellation,
                                @RequestParam("bankAddress")String bankAddress);

    @RequestMapping("/accounts/mobile/getBankCardRawData")
    IndividualDatumVO getBankCardRawData(@RequestParam("gameId")Integer gameId,
                                         @RequestParam("agentId")Integer agentId);

    @RequestMapping("/accounts/mobile/winAndLoseDetail")
    List<WinOrLoseDetailVO> winAndLoseDetail(@RequestParam("userId")Integer userId,
                                             @RequestParam("beginTime")String beginTime,
                                             @RequestParam("endTime") String endTime);

    /**
     * 游戏公告
     *
     * @param agentId
     * @return
     */
    @RequestMapping("/accounts/mobile/getGameNews")
    List<SystemNewsVO> getGameNews(@RequestParam("agentId")Integer agentId);
    
    @RequestMapping("/accounts/mobile/saveGameRecordOther")
	void saveGameRecordOther(@RequestParam("gameCode") String gameCode);
    
    @RequestMapping("/accounts/mobile/saveGameRecord")
	void saveGameRecord(@RequestParam("gameCode") String gameCode);

}
