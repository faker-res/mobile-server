package com.lzkj.mobile.vo;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class MemberRechargeVO {

	private BigDecimal Balance;
	private BigDecimal presentScore = BigDecimal.ZERO;
	private BigDecimal expenditureScore = BigDecimal.ZERO;
	private String collectDate;
	private String typeName;
}
