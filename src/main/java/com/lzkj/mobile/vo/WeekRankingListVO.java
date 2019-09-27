package com.lzkj.mobile.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class WeekRankingListVO {
    private Integer userId;
    private Integer gameId;
    private Integer parentId;
    private Integer ranking;
    private BigDecimal weekRewardScore;
    private String insertTime;
    private BigDecimal weekScore;

}
