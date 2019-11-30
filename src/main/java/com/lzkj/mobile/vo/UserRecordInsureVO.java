package com.lzkj.mobile.vo;

import java.math.BigDecimal;

import lombok.Data;
@Data
public class UserRecordInsureVO {

	private String dateTime;
	private Integer tradeType;
	private BigDecimal changeScore;
	private BigDecimal beforeScore;
}
