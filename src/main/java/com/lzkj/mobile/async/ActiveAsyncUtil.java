package com.lzkj.mobile.async;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.lzkj.mobile.client.AccountsServiceClient;
import com.lzkj.mobile.client.NativeWebServiceClient;

import lombok.extern.slf4j.Slf4j;

@Component(value = "ActiveAsyncUtil")
@Slf4j
public class ActiveAsyncUtil {

	@Autowired
	private NativeWebServiceClient nativeWebServiceClient;
	
	@Autowired
	private AccountsServiceClient accountsServiceClient;
	
	@Async
	public void activityBetAmountAdvance(Integer userId, Integer parentId, Integer level, Integer kindId,
			BigDecimal betAmount, String betDate, Integer gameKindId) {
		log.info("用户{}开始推动打码活动，参数：kindId:{}，gameKindId:{}，betAmount:{}，parentId:{}，level:{}，betDate:{}", userId, kindId,gameKindId, betAmount, parentId, level, betDate);
		nativeWebServiceClient.activityBetAmountAdvanceByTT(userId, parentId, level, kindId, betAmount, betDate,gameKindId);
	}
	
	@Async
	public void saveEsGameRecordOther(String gameCode) {
		log.info("将数据添加到es GameRecordOther中去,局号:{}",gameCode);
		accountsServiceClient.saveGameRecordOther(gameCode);
	}
	
  	@Async
	public void saveEsGameRecord(String gameCode) {
		log.info("将数据添加到es GameRecordOther中去,局号:{}",gameCode);
		accountsServiceClient.saveGameRecord(gameCode);
	}
}
