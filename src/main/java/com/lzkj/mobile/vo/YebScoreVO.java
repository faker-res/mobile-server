package com.lzkj.mobile.vo;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class YebScoreVO {
	
	private BigDecimal dayPresentScore = BigDecimal.ZERO;
	
	private BigDecimal historyPresentScore = BigDecimal.ZERO;
}
