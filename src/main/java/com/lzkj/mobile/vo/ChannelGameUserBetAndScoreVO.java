package com.lzkj.mobile.vo;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class ChannelGameUserBetAndScoreVO {

	private String betTime;
	
	private String channelUniqueId;
	
	private BigDecimal betAmount;
	
	private BigDecimal score;
	
	private String gameName;
}
