package com.lzkj.mobile.vo;

import java.math.BigDecimal;

public class UserGameScoreInfoVO {
	private int chartId;
	private BigDecimal score;
	
	
	public BigDecimal getScore() {
		return score;
	}
	public void setScore(BigDecimal score) {
		this.score = score;
	}
	public int getChartId() {
		return chartId;
	}
	public void setChartId(int chartId) {
		this.chartId = chartId;
	}
}
