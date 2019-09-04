package com.lzkj.mobile.vo;

import java.math.BigDecimal;

public class ScoreRankVO {
	private int userId;
	
	private BigDecimal score;

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public BigDecimal getScore() {
		return score;
	}

	public void setScore(BigDecimal score) {
		this.score = score;
	}
}
