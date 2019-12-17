package com.lzkj.mobile.client;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.lzkj.mobile.vo.AccReportVO;
import com.lzkj.mobile.vo.AccountsInfoVO;
import com.lzkj.mobile.vo.AccountsLevelVO;
import com.lzkj.mobile.vo.ActivityRedEnvelopeRewardVO;
import com.lzkj.mobile.vo.ActivityRedEnvelopeVO;
import com.lzkj.mobile.vo.ApplyRecordPageVo;
import com.lzkj.mobile.vo.BindPhoneVO;
import com.lzkj.mobile.vo.ChannelGameUserBetAndScoreVO;
import com.lzkj.mobile.vo.CommonPageVO;
import com.lzkj.mobile.vo.MailVO;
import com.lzkj.mobile.vo.PersonalReportVO;
import com.lzkj.mobile.vo.RedEnvepoleRulesVO;
import com.lzkj.mobile.vo.SystemStatusInfoVO;
import com.lzkj.mobile.vo.UserCodeDetailsVO;
import com.lzkj.mobile.vo.UserInfoVO;
import com.lzkj.mobile.vo.UserInformationVO;
import com.lzkj.mobile.vo.VipLevelRewardVO;
import com.lzkj.mobile.vo.VisitorBindResultVO;

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

    //查询当前用户上级代理佣金
    @RequestMapping("accounts/mobile/queryRatioUserInfo")
    BigDecimal queryRatioUserInfo(@RequestParam("gameId") Integer gameId);

    //查询当前用户上级代理佣金
    @RequestMapping("accounts/mobile/queryRegisterMobile")
    Integer queryRegisterMobile(@RequestParam("phone") String phone);

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

    @RequestMapping("/accounts/mobile/getMailsInfo")
    List<MailVO> getMailsInfo(@RequestParam("gameId")Integer gameId, @RequestParam("agentId")Integer agentId);

    @RequestMapping("/accounts/mobile/openMail")
    Boolean openMail(@RequestParam("ids") List<Integer> ids);

    @RequestMapping("/accounts/mobile/deleteMail")
    Boolean deleteMail(@RequestParam("ids") List<Integer> ids);

    @RequestMapping("/accounts/mobile/getCollectionName")
    String getGameItem(@RequestParam("kindId") Integer kindId);

    @RequestMapping("/accounts/mobile/resetInsurePwd")
    Map<String, Object> resetInsurePwd(@RequestParam("userId") Integer userId,@RequestParam("oldPwd") String oldPwd,
                                       @RequestParam("newPwd") String newPwd);

    @RequestMapping("/accounts/mobile/userBankInformation")
    int getUserBankInformation(@RequestParam("bankNo") String bankNo);

    @RequestMapping("/accounts/mobile/totalMail")
    int totalMail(@RequestParam("gameId")Integer gameId, @RequestParam("agentId")Integer agentId);

    @RequestMapping("/accounts/mobile/getUserVipLevel")
    VipLevelRewardVO getUserVipLevel(@RequestParam("userId")Integer userId);

    @RequestMapping("/accounts/mobile/getVipLevelConfig")
    List<VipLevelRewardVO> getVipLevelConfig(@RequestParam("parentId")Integer parentId);

    @RequestMapping("/accounts/mobile/getUsersInfo")
    UserInformationVO getUsersInfo(@RequestParam("userId")Integer userId);

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
    UserCodeDetailsVO cashFlowDetails(@RequestParam("userId") Integer userId, @RequestParam("agentId") Integer agentId);

    @RequestMapping("/accounts/mobile/getRedEnvelopeReward")
    List<ActivityRedEnvelopeRewardVO> getRedEnvelopeReward(@RequestParam("userId") Integer userId,@RequestParam("parentId") Integer parentId);

    @RequestMapping("/accounts/mobile/getReceivingRedEnvelope")
    Integer getReceivingRedEnvelope(@RequestParam("userId") Integer userId,@RequestParam("score") BigDecimal score,@RequestParam("ip") String ip,@RequestParam("machineId") String machineId,@RequestParam("typeId") Integer typeId);

    @RequestMapping("/accounts/mobile/verifyPassword")
    String verifyPassword(@RequestParam("userId") Integer userId);

    @RequestMapping("/accounts/mobile/getRedEnvelope")
    List<ActivityRedEnvelopeVO> getRedEnvelope(@RequestParam("userId") Integer userId,@RequestParam("parentId") Integer parentId);


    @RequestMapping("/accounts/mobile/getOpenMailList")
    List<MailVO> getOpenMailList(@RequestParam("ids")List<Integer> ids);
    
    @RequestMapping("/accounts/mobile/getRedEnvepoleRules")
    RedEnvepoleRulesVO getRedEnvepoleRules(@RequestParam("parentId") Integer parentId);
    
    @RequestMapping("/accounts/mobile/getReceiveRedEnvelopeRecord")
    Integer getReceiveRedEnvelopeRecord(@RequestParam("userId") Integer userId);
}
