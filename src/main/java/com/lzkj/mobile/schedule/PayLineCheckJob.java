package com.lzkj.mobile.schedule;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.lzkj.mobile.client.TreasureServiceClient;
import com.lzkj.mobile.util.HttpRequest;
import com.lzkj.mobile.vo.PayUrlListVO;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class PayLineCheckJob {
	public static String PAY_LINE = "http://lzapp-form.chengdulan.com";
	
	@Autowired
	private TreasureServiceClient treasureMapper;
	
	@Scheduled(fixedDelay = 60000)//fixedRate
	public void check() {
		log.info("===开始检测支付线路");
 		List<PayUrlListVO> list = treasureMapper.getPayLineConfig();
 		
 		//域名提交
 		for(int i = 0; i<list.size(); i++) {
			String url = list.get(i).getSendUrl();
 			String res = HttpRequest.sendGet(url + "/pay/index", "");
 			if(res.equals("200")) {
 				PAY_LINE = url;
 				log.info("{}响应正常", url);
 				return;
 			}
 			log.info("{}线路有问题", url);
 		}
 		log.info("===结束检测支付线路");
	}
	
}
