package com.lzkj.mobile.vo;

import java.math.BigDecimal;

public class GlobalSpreadInfo {
	private int id;
	
	private int registerGrantScore;
	
	private int playTimeCount;
	
	private int playTimeGrantScore;
	
	private BigDecimal fillGrantRate;
	
	private BigDecimal balanceRate;
	
	private int minBalanceScore;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getRegisterGrantScore() {
		return registerGrantScore;
	}

	public void setRegisterGrantScore(int registerGrantScore) {
		this.registerGrantScore = registerGrantScore;
	}

	public int getPlayTimeCount() {
		return playTimeCount;
	}

	public void setPlayTimeCount(int playTimeCount) {
		this.playTimeCount = playTimeCount;
	}

	public int getPlayTimeGrantScore() {
		return playTimeGrantScore;
	}

	public void setPlayTimeGrantScore(int playTimeGrantScore) {
		this.playTimeGrantScore = playTimeGrantScore;
	}

	public BigDecimal getFillGrantRate() {
		return fillGrantRate;
	}

	public void setFillGrantRate(BigDecimal fillGrantRate) {
		this.fillGrantRate = fillGrantRate;
	}

	public BigDecimal getBalanceRate() {
		return balanceRate;
	}

	public void setBalanceRate(BigDecimal balanceRate) {
		this.balanceRate = balanceRate;
	}

	public int getMinBalanceScore() {
		return minBalanceScore;
	}

	public void setMinBalanceScore(int minBalanceScore) {
		this.minBalanceScore = minBalanceScore;
	}
	
	
	
}
