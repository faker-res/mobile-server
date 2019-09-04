package com.lzkj.mobile.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class MyRewardVO {
	private Integer gameId;
	
	private BigDecimal totalReward; 
	
	private BigDecimal yesLiquidation;
	
	private BigDecimal alreadyLiquidation; 
	
	private List<MyQmTxRecord> list;
}
