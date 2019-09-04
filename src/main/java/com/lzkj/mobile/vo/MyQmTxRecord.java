package com.lzkj.mobile.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class MyQmTxRecord {
	private String liquidationTime;
	private String orderId;
	private BigDecimal liquidationReward;
	private String liquidationType;
	private String liquidationAccount;
	private String status;
}
