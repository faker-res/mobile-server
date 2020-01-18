package com.lzkj.mobile.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class MemberRechargeVO {

	private BigDecimal Balance;
	private BigDecimal presentScore = BigDecimal.ZERO;
	private BigDecimal expenditureScore = BigDecimal.ZERO;
	private String collectDate;
	private String typeName;
	private String collectNote;
}
