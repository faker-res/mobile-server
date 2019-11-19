package com.lzkj.mobile.vo;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class VipLevelRewardVO {

	private Integer vipLevel;	//vip等级
	
	private BigDecimal vipIntegral;  //积分
	
	private BigDecimal vipRankReward;
	
	private BigDecimal weekReward;
	
	private BigDecimal monthReward;
	
	private BigDecimal dayReward;
	
	private BigDecimal yearReward;
	
	private Integer status;
	
	private BigDecimal total;
	
	private BigDecimal vipVersion;
	
	private BigDecimal yebRate;
	
}
