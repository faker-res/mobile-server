package com.lzkj.mobile.vo;

import java.math.BigDecimal;

public class UserScoreRankVO {
	private String nickName;
	private BigDecimal score;
	private Integer rank;
	
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public BigDecimal getScore() {
		return score;
	}
	public void setScore(BigDecimal score) {
		this.score = score;
	}
	public Integer getRank() {
		return rank;
	}
	public void setRank(Integer rank) {
		this.rank = rank;
	}
	
	
}
