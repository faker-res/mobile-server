package com.lzkj.mobile.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class MyPlayerVO {
	private Integer gameId;
	private String nickName;
	private BigDecimal weekShare;
	private BigDecimal weekDrictMemberShare;
	private int drictMemberCount;
	private int memberCount;
	private String rationType;
	private BigDecimal zzQmRatio;
}
