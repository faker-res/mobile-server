package com.lzkj.mobile.schedule;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.lzkj.mobile.client.TreasureServiceClient;
import com.lzkj.mobile.util.HttpRequest;
import com.lzkj.mobile.vo.DomainInfoVO;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class RequestDomainSchedule {
	
	public static String domainSendUrl;
	public static String formSendUrl;
	
	@Autowired
	 private TreasureServiceClient treasureMapper;
	@Scheduled(cron = "0 0/1 * * * *")
	public void requestDomain() {
		log.info("===开始ping支付域名");
 		List<DomainInfoVO> list = treasureMapper.getDomainInfo();
 		String date = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date());
 		//域名提交
 		for(int i =0;i<list.size();i++) {
 			if(list.get(i).getType() == 1) {
 				String url = list.get(i).getSendUrl();
 	 			String res = HttpRequest.sendPost(url + "pay/index", "");
 	 			if(res.equals("200")) {
 	 				domainSendUrl = url;
 	 				return;
 	 			}
 	 			treasureMapper.updateDomainRecord(1, date, list.get(i).getId());
 			}else {//表单提交
 				String url = list.get(i).getSendUrl();
 	 			String res = HttpRequest.sendPost(url + "pay/index", "");
 	 			if(res.equals("200")) {
 	 				formSendUrl = url;
 	 				return;
 	 			}
 	 			treasureMapper.updateDomainRecord(1, date, list.get(i).getId());
 			}
 			
 		}
 		log.info("===结束ping支付域名");
	}
	
}
