package com.lzkj.mobile.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 会员充值信息
 *
 * @author xxx
 */
@Data
public class MemberRechargeVO {

	private BigDecimal Balance;

	private BigDecimal presentScore = BigDecimal.ZERO;

	private BigDecimal expenditureScore = BigDecimal.ZERO;

	private String collectDate;

	private String typeName;

	private String collectNote;
}
