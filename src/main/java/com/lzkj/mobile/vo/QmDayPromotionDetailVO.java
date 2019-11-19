package com.lzkj.mobile.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class QmDayPromotionDetailVO {
    private Integer gameId;               //用户游戏ID

    private Integer memberCount;          //团员人数

    private Integer newMemberCount;       //新增成员总数

    private Integer directlyMemberCount;     //直属玩家

    private Integer newDirectlyMemberCount; //新增直属玩家数、

    private String registerDate ; //注册时间

    private BigDecimal todayBet; //今日流水

    private BigDecimal totalBet;//总流水

    private BigDecimal todayTeamBet; //今日团队业绩

    private BigDecimal todayDirectlyBet ;//今日直属业绩

    private BigDecimal historyCommission;// 历史总佣金

    private BigDecimal yesterdayCommission;//昨日佣金

    private BigDecimal unReceipt;//未领取佣金

    private Integer directlyAgent ;//直属代理

    private Integer newDirectlyAgent ;//新增直属代理
}
