package com.lzkj.mobile.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class MyRewardRecordVO {
	private String rewardTime;
	private BigDecimal weekAchievement;
	private BigDecimal weekTeamAchievement;
	private BigDecimal weekPersonalAchievement;
	private BigDecimal rewardScore;
	private String status;
}	
