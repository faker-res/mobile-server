package com.lzkj.mobile.vo;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class MemberRechargeVO {

	private BigDecimal Balance;
	private BigDecimal presentScore;
	private String collectDate;
	private String typeName;
}
