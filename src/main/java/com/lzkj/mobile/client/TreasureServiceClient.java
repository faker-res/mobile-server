package com.lzkj.mobile.client;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.lzkj.mobile.vo.AgentCompanyRecordVO;
import com.lzkj.mobile.vo.CompanyPayVO;
import com.lzkj.mobile.vo.DayUserAbsScoreVO;
import com.lzkj.mobile.vo.GatewayInfo;
import com.lzkj.mobile.vo.GlobalSpreadInfo;
import com.lzkj.mobile.vo.LotteryConfigVO;
import com.lzkj.mobile.vo.LuckyTurntableConfigurationVO;
import com.lzkj.mobile.vo.OnLineOrderVO;
import com.lzkj.mobile.vo.PayInfoVO;
import com.lzkj.mobile.vo.RecordInsurePageVO;
import com.lzkj.mobile.vo.ScoreRankPageVO;
import com.lzkj.mobile.vo.ShareDetailInfoVO;
import com.lzkj.mobile.vo.TpayOwnerInfoVO;
import com.lzkj.mobile.vo.UserGameScoreInfoVO;
import com.lzkj.mobile.vo.ViewPayInfoVO;
import com.lzkj.mobile.vo.YebConfigVO;
import com.lzkj.mobile.vo.yebProfitDetailsVO;

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

	@RequestMapping("treasure/mobile/getPayList")
	Map<String, List<PayInfoVO>> getPayList(@RequestParam("userId") Integer userId);

	@RequestMapping("treasure/mobile/getGameRecord")
	Object getGameRecord(@RequestParam("pageIndex") int pageIndex, @RequestParam("pageSize") int pageSize, @RequestParam("userId") Integer userId, @RequestParam("kindId") Integer kindId);

	/**
	 * 支付跳转 获取渠道数据
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
	 *保存充值订单记录
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
	 * @param shareDetailInfoVO
	 * @return
	 */
	@RequestMapping("treasure/mobile/filliedOnline")
	Map<String,Object> filliedOnline(@RequestBody ShareDetailInfoVO shareDetailInfoVO);

	/**
	 * 查询公司支付通道
	 * @param agentId
	 * @return
	 */
	@RequestMapping("treasure/mobile/getCompanyPay")
	List<Object> getCompanyPay(@RequestParam("agentId") Integer agentId);

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
	 * 生成公司充值订单
	 */
	@RequestMapping("treasure/mobile/insertRecord")
	Map insertRecord(@RequestParam("agentId") Integer agentId, @RequestParam("userId") Integer userId, @RequestParam("gameId") Integer gameId,
                     @RequestParam("payId") Integer payId, @RequestParam("orderAmount") BigDecimal orderAmount, @RequestParam("remarks") String remarks, @RequestParam("account") String account,
                     @RequestParam("paymentAccount") String paymentAccount, @RequestParam("paymentName") String paymentName);

	@RequestMapping("treasure/mobile/getUpdateAddress")
	GatewayInfo getUpdateAddress(@RequestParam("agentId") Integer agentId);

	@RequestMapping("treasure/mobile/updateMerchantOrderId")
	void updateMerchantOrderId(@RequestParam("ownerOrderId") String ownerOrderId, @RequestParam("merchantOrderId") String merchantOrderId);

	@RequestMapping("treasure/mobile/updatePassagewayResponse")
	void updatePassagewayResponse(@RequestParam("ownerOrderId") String ownerOrderId, @RequestParam("passagewayResponse") String passagewayResponse);

	//余额宝是否开启
	@RequestMapping("treasure/mobile/getYebIsOpen")
	YebConfigVO getYebIsOpen(@RequestParam("agentId")Integer agentId);

	//余额宝收益明细
	@RequestMapping("treasure/mobile/getyebProfitDetailsList")
	List<yebProfitDetailsVO> getyebProfitDetailsList(@RequestParam("userId")Integer userId,@RequestParam("pageIndex")Integer pageIndex,@RequestParam("pageSize")Integer pageSize);

	//收益总数
	@RequestMapping("treasure/mobile/getYwbAfterInfo")
	yebProfitDetailsVO getYwbAfterInfo(@RequestParam("userId")Integer userId);

	@RequestMapping("/treasure/mobile/getUserGameScore")
	Map<String,BigDecimal> getUserGameScore(@RequestParam("userId")Integer userId);
	
	@RequestMapping("/treasure/mobile/getTodayRankingList")
    public List<DayUserAbsScoreVO> getTodayRankingList(@RequestParam("parentId")Integer parentId);
	

	@RequestMapping("/treasure/mobile/getLucky")
    public LuckyTurntableConfigurationVO getLucky(@RequestParam("agentId")Integer agentId);

	@RequestMapping("/treasure/mobile/getMyTodayRanking")
    public DayUserAbsScoreVO getMyTodayRanking(@RequestParam("userId")Integer userId);

}

