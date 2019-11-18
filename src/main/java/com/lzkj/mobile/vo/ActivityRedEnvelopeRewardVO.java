package com.lzkj.mobile.vo;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class ActivityRedEnvelopeRewardVO {
	/**
	 * 每日充值红包奖励
	 */
	private BigDecimal dayRedEnvelopeAmount;  //每日充值红包奖励
	/**
	 * 累计充值红包奖励
	 */
	private BigDecimal accumulatedRedEnvelopeAmount;  //累计充值红包奖励
	/**
	 * 每日打码量红包奖励
	 */
	private BigDecimal dayBetAmountRedEnvelopeAmount;  //每日打码量红包奖励
	/**
	 * 累计打码量红包奖励
	 */
	private BigDecimal cumulativeBetAmountRedEnvelope;  //累计打码量红包奖励
	
	private Integer status;  //0可以领取1不可领取
	
	private Integer loginRedEnvelopeReward;		//红包是否领取 0  1 
}
