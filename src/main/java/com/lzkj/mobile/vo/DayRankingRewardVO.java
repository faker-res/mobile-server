package com.lzkj.mobile.vo;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class DayRankingRewardVO {
	private int id;
	private int ranking;
	private Integer userId;
	private Integer gameId;
	private String nickName;
	private BigDecimal score;
	private BigDecimal rewardScore;
	private int rewardStatus;
}
