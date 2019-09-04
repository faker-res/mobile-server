package com.lzkj.mobile.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class QmAchievementVO {

    private String time;

    private BigDecimal dayTotalAchievement;

    private BigDecimal dayTeamAchievement;

    private BigDecimal dayPersonalAchievement;
    
    private BigDecimal estimatedRevenue;

    private BigDecimal dayRevenue;
}
