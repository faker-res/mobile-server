package com.lzkj.mobile.vo;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class PersonalReportVO {

	private BigDecimal totalProfit;
	private BigDecimal betAmount;
	private BigDecimal score;
	private BigDecimal backwater;
}
