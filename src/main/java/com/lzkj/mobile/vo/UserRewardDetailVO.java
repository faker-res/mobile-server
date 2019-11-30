package com.lzkj.mobile.vo;

import java.math.BigDecimal;

import lombok.Data;
@Data
public class UserRewardDetailVO {

	private String insertDate;
	private BigDecimal amount;
	private BigDecimal score;
	private String type;
}
