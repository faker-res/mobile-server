package com.lzkj.mobile.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class QmDayPromotionDetailVO {
    private int userId;
    private BigDecimal allReward;
    private int dayDirectMemberCount;
    private int dayOtherMemberCount;
    private BigDecimal dayDirectRewardCount;
    private BigDecimal DayOtherRewardCount;
    private String today;
}
