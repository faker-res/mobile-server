package com.lzkj.mobile.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class SelfMoneyVO {

    private BigDecimal totalPayAmount;    //总充值
    private BigDecimal todayPayAmount;    //今日充值
    private BigDecimal yesterdayPayAmount;    //昨日充值
    private BigDecimal weekPayAmount;     //本周充值


    private BigDecimal totalWithdrawalAmount;   //总提现
    private BigDecimal todayWithdrawalAmount;   //今日提现
    private BigDecimal yesterdayWithdrawalAmount;   //昨日提现
    private BigDecimal weekWithdrawalAmount;    //本周提现


    private Integer totalMemberCount;     //总新增人数
    private Integer todayMemberCount;   //今日新增人数
    private Integer yesterdayMemberCount;   //昨日新增人数
    private Integer weekMemberCount;    //本周新增人数

/*    private Integer totalMemberCount;     //总派彩
    private BigDecimal todayMemberCount;   //今日派彩
    private BigDecimal yesterdayMemberCount;   //昨日派彩
    private BigDecimal weekMemberCount;    //本周派彩*/

    private BigDecimal totalLoseOrWin;     //总中奖
    private BigDecimal todayLoseOrWin;   //今日中奖
    private BigDecimal yesterdayLoseOrWin;   //昨日中奖
    private BigDecimal weekLoseOrWin;    //本周中奖

    private BigDecimal totalBetAmount;    //总投注
    private BigDecimal todayBetAmount;    //今日投注
    private BigDecimal yesterdayBetAmount;    //昨日投注
    private BigDecimal weekBetAmount;    //本周投注

    private BigDecimal totalDiscount;    //总优惠
    private BigDecimal todayDiscount;    //今日优惠
    private BigDecimal yesterdayDiscount;    //昨日优惠
    private BigDecimal weekDiscount;    //本周优惠

    private BigDecimal totalReward;    //总佣金
    private BigDecimal todayReward;    //今日佣金
    private BigDecimal yesterdayReward;    //昨日佣金
    private BigDecimal weekReward;    //本周佣金

}
