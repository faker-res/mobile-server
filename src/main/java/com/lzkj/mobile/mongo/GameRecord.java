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
	private String account;
	private Long startTime;
	private Long endTime;
	private String serverName;
	private BigDecimal score;
	private BigDecimal revenue;
	private Integer kindId;
	private String gameCode;
	private String h5Account;
	private String h5SiteCode;	
	private BigDecimal betAmount;
	private String detail;
	
	
}
