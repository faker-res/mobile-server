package com.lzkj.mobile.client;

import java.util.List;

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
}
