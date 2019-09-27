package com.lzkj.mobile.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class AgencyEqualReward {
	private Integer parentId;
	private Integer ranking;
	private BigDecimal rewardScore;
    private BigDecimal weekRewardScore;
    private BigDecimal monthRewardScore;
}
