package com.lzkj.mobile.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 *我的推广-- 推广详情
 */
@Data
public class MyPopularizeVO {

   private Integer parent;               //上级ID

   private Integer gameId;               //用户游戏ID

   private Integer memberCount;          //团员人数

   private Integer newMemberCount;       //今日新增成员总数

   private Integer directlyMemberCount;     //直属玩家

   private Integer newDirectlyMemberCount; //今日新增直属玩家数

   private Integer directlyAgent ;//直属代理

   private Integer newDirectlyAgent ;//今日新增直属代理

   private BigDecimal todayTeamBet; //今日团队业绩

   private BigDecimal todayDirectlyBet ;//今日直属业绩

   private BigDecimal todayCommission; //今日佣金预估

   private BigDecimal yesterdayCommission;//昨日佣金

   private BigDecimal historyCommission; //历史总佣金

   private BigDecimal unReceipt;//未领取佣金

}
