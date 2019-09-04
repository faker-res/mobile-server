package com.lzkj.mobile.vo;

import java.util.List;

public class ScoreRankPageVO {
	private List<ScoreRankVO> scoreRankList;
	
	private int pageCount;
	
	private int recordCount;

	public List<ScoreRankVO> getScoreRankList() {
		return scoreRankList;
	}

	public void setScoreRankList(List<ScoreRankVO> scoreRankList) {
		this.scoreRankList = scoreRankList;
	}

	public int getPageCount() {
		return pageCount;
	}

	public void setPageCount(int pageCount) {
		this.pageCount = pageCount;
	}

	public int getRecordCount() {
		return recordCount;
	}

	public void setRecordCount(int recordCount) {
		this.recordCount = recordCount;
	}
	
	
}
