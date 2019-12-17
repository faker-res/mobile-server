package com.lzkj.mobile.vo;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class ActivityRedEnvelopeVO {

	private Integer activityId;
	
	private BigDecimal amount;
	
	private BigDecimal wctj;
	
	private BigDecimal ycz;
	
	private Integer status;			//0  可领取  1 活动已结束  2  已领取  3 未达到赠送条件
	
	private Integer typeId;
}
