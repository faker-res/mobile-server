package com.lzkj.mobile.client;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.lzkj.mobile.vo.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "nativeweb-service")
public interface NativeWebServiceClient {

    @RequestMapping("/nativeWeb/mobile/configInfo")
    ConfigInfo getConfigInfo(@RequestParam("configKey") String configKey);

    @RequestMapping("nativeWeb/mobile/getGameNotice")
    List<NewsVO> getGameNotice(@RequestParam("classID") Integer classID, @RequestParam("agentId") Integer agentId);

    @RequestMapping("nativeWeb/mobile/getMobileNotice")
    List<NewsVO> getMobileNotice(@RequestParam("agentId") Integer agentId);

    @RequestMapping("nativeWeb/mobile/getOrderRecord")
    AwardOrderPageVo getAwardOrder(@RequestParam("page") int i, @RequestParam("number") Integer numberN, @RequestParam("userId") Integer userNo);

    @RequestMapping("nativeWeb/mobile/getActivityList")
    Object getActivityList(@RequestParam("agentId") Integer agentId);

    @RequestMapping("/nativeWeb/mobile/getShowImgUrl")
    String getShowImgUrl(@RequestParam("agentId") Integer agentId);

	@RequestMapping("/nativeweb/manager/active/getActivityType")
	List<ActivityTypeVO> getActivityType();

	@RequestMapping("/nativeweb/manager/active/getActivityListByMobile")
	CommonPageVO getActivityListByMobile(@RequestParam("agentId") Integer agentId,@RequestParam("gameCategory")Integer gameCategory,@RequestParam("pageIndex")Integer pageIndex);


	@RequestMapping("nativeWeb/mobile/getNoticeTitile")
	List<NewsVO> getNoticeTitile(@RequestParam("classID") Integer classID, @RequestParam("agentId") Integer agentId);

	@RequestMapping("nativeWeb/mobile/getNoticeDetail")
	public String getNoticeDetail(@RequestParam("newsId")Integer newsId);

	@RequestMapping("/nativeweb/manager/active/getYuebaoDescription")
    YebDescriptionVO getYuebaoDescription(@RequestParam("agentId")Integer agentId);


    @RequestMapping("nativeWeb/mobile/userApply")
    Map<String, Object> userApply(@RequestParam("userId") Integer userId, @RequestParam("activeId") Integer activeId,
                                  @RequestParam("amount") BigDecimal amount,@RequestParam("agentId")Integer agentId,@RequestParam("memo")String memo);

    @RequestMapping("nativeWeb/mobile/getApplyList")
    CommonPageVO<ActivityRecordVO> getApplyList(@RequestParam("userId") Integer userId, @RequestParam("pageIndex") Integer pageIndex,
                                                @RequestParam("pageSize") Integer pageSize,@RequestParam("kindType")Integer kindType);
    
    @RequestMapping("/nativeWeb/mobile/getActivityByTrigger")
	Object getActivityByTrigger(@RequestParam("userId")Integer userId, @RequestParam("agentId")Integer agentId, @RequestParam("method")Integer method, @RequestParam("device")Integer device);
    
    @RequestMapping("/nativeweb/manager/active/activityBetAmountAdvanceByTT")
	void activityBetAmountAdvanceByTT(@RequestParam("userId")Integer userId, @RequestParam("parentId")Integer parentId, @RequestParam("level")Integer level, @RequestParam("kindId")Integer kindId, 
			@RequestParam("betAmount")BigDecimal betAmount,@RequestParam("betDate")String betDate,@RequestParam("gameKindId") Integer gameKindId);
    
    @RequestMapping("/nativeWeb/mobile/getAccountsActivity")
	Object getAccountsActivity(@RequestParam("userId")Integer userId, @RequestParam("activityId")Integer activityId,@RequestParam("agentId")Integer agentId,@RequestParam("ruleType")Integer ruleType);
    
    @RequestMapping("/nativeWeb/mobile/getActivityApplication")
	Object getActivityParameter(@RequestParam("activityId")Integer activityId,@RequestParam("agentId")Integer agentId);
    
    @RequestMapping("/nativeWeb/mobile/getAccountsApplication")
	Object getAccountsApplication(@RequestParam("id")Integer id, @RequestParam("userId")Integer userId, @RequestParam("activityId")Integer activityId, @RequestParam("agentId")Integer agentId,
			@RequestParam("ruleType")Integer ruleType,@RequestParam("ruleId")Integer ruleId,@RequestParam("appDate")String appDate);
}
