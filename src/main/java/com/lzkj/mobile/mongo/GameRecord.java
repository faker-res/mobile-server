package com.lzkj.mobile.mongo;

import java.math.BigDecimal;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document
public class GameRecord {
	private Integer playerId;
	private Integer serverId;
	private String gameName;
	private Long startTime;
	private Long endTime;
	private String serverName;
	private BigDecimal score;
	private BigDecimal revenue;
	private Integer kindId;
    private String gameHandCode;
	private String gameCode;
	private String siteCode;
	private String account;
	private BigDecimal betAmount;
	private String detail;
	
	
}
