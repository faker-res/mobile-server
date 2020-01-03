package com.lzkj.mobile.vo;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class AccountsTask {
	private String imageUrl;
	private int innings;
	private BigDecimal progress;
	private String taskName;
	private int taskId;
	private BigDecimal standardAwardGold;
	private int taskStatus;
	private int taskPeriod;
	private String taskValue;
	private int taskType;
}
