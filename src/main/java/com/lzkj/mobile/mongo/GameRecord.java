package com.lzkj.mobile.mongo;

import java.math.BigDecimal;

import com.alibaba.fastjson.JSONObject;

import lombok.Data;

@Data
public class GameRecord {
	private Integer playerId;
	private Integer serverId;
	private String gameName;
	private Long startTime;
	private Long endTime;
	private String serverName;
	private BigDecimal score;
	private BigDecimal revenue;
	private Integer gameId;
	private String gameCode;
	private String siteCode;
	private String account;
	private BigDecimal betAmount;
	private JSONObject detail;
	
	
}
