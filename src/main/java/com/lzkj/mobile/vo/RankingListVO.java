package com.lzkj.mobile.vo;

import java.math.BigDecimal;
import java.util.List;

import lombok.Data;

@Data
public class RankingListVO {
	private int id;
	private int myRanking;
	private int rewardStatus;
	private BigDecimal score = BigDecimal.ZERO;
	private BigDecimal myReward = BigDecimal.ZERO;
	private List<Ranking> rankingList;
	
	@Data
	public static class Ranking {
		private Integer gameId;
		private String nickName;
		private BigDecimal score;
		private BigDecimal reward;
		private int ranking;
	}
}
