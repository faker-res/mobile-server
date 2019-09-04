package com.lzkj.mobile.client;

import com.lzkj.mobile.vo.AwardOrderPageVo;
import com.lzkj.mobile.vo.ConfigInfo;
import com.lzkj.mobile.vo.NewsVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

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
}
