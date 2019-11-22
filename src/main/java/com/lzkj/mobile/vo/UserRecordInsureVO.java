package com.lzkj.mobile.vo;

import java.math.BigDecimal;

import lombok.Data;
@Data
public class UserRecordInsureVO {

	private String CollectDate;
	private Integer TradeType;
	private BigDecimal SwapScore;
	private BigDecimal VarietyScore;
}
