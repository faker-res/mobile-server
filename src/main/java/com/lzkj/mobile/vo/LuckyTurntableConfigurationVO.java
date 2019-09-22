package com.lzkj.mobile.vo;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class LuckyTurntableConfigurationVO {
	private Integer id;
	private BigDecimal codeAmount;
	private BigDecimal rechargeAmount;
	private Integer parentId;
	private Integer codeAmountIsOpen;
	private Integer rechargeAmountIsOpen;
	private Integer codeAmountFrequency;
	private Integer rechargeAmountFrequency;
	private Integer cell1;
	private Integer cell2;
	private Integer cell3;
	private Integer mainSwitch;
	private Integer goldIsOpen;
	
}
