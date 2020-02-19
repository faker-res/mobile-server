package com.lzkj.mobile.async;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.lzkj.mobile.client.AccountsServiceClient;
import com.lzkj.mobile.client.NativeWebServiceClient;
import com.lzkj.mobile.util.HttpRequest;

import lombok.extern.slf4j.Slf4j;

@Component(value = "ActiveAsyncUtil")
@Slf4j
@Async
public class ActiveAsyncUtil {

	@Autowired
	private NativeWebServiceClient nativeWebServiceClient;
	
	@Autowired
	private AccountsServiceClient accountsServiceClient;
	
	 @Value("${server.url}")
	 private String serverUrl;
	 
	public void activityBetAmountAdvance(Integer userId, Integer parentId, Integer level, Integer kindId,
			BigDecimal betAmount, String betDate, Integer gameKindId) {
		log.info("用户{}开始推动打码活动，参数：kindId:{}，gameKindId:{}，betAmount:{}，parentId:{}，level:{}，betDate:{}", userId, kindId,gameKindId, betAmount, parentId, level, betDate);
		nativeWebServiceClient.activityBetAmountAdvanceByTT(userId, parentId, level, kindId, betAmount, betDate,gameKindId);
	}
	
	public void saveEsGameRecordOther(List<String> codeList) {
		log.info("将数据添加到es GameRecordOther中");
		accountsServiceClient.saveGameRecordOtherByList(codeList);
	}

	public void saveEsGameRecord(List<String> codeList) {
		log.info("将数据添加到es GameRecord中");
		accountsServiceClient.saveGameRecordByList(codeList);
	}

	public void goldChanges(Integer userId, String score, String vipLevel) {
		String msg = "{\"msgid\":7,\"userId\":" + userId + ", \"score\":" + score + ",\"insuranceScore\":" + 0 +
                ", \"VipLevel\":" + vipLevel + ", \"type\":" + 0 + ", \"Charge\":" + 0 + "}";
        log.info("调用金额变更指令:{}, 返回：" + HttpRequest.sendPost(this.serverUrl, msg), msg);
	}
}
