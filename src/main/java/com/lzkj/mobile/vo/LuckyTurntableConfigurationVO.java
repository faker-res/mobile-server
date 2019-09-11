package com.lzkj.mobile.vo;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class LuckyTurntableConfigurationVO {
	private Integer id;
	private BigDecimal gold;
	private BigDecimal codeAmount;
	private BigDecimal rechargeAmount;
	private Integer parentId;
	private Integer codeAmountIsOpen;
	private Integer goldIsOpen;
	private Integer rechargeAmountIsOpen;
	private Integer codeAmountFrequency;
	private Integer rechargeAmountFrequency;
	
}
