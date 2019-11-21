package com.lzkj.mobile.vo;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class YebInterestRateVO {

	private Integer vipLevel;
	
	private BigDecimal dayYebRate;
	
	private BigDecimal monthYebRate;
	
	private BigDecimal yearYebRate;
}
