package com.lzkj.mobile.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class SelfReportVO {

    private BigDecimal totalBetAmount;      //总投注
    private BigDecimal qpBetAmount;         //棋牌总投注
    private BigDecimal byBetAmount;         //捕鱼总投注
    private BigDecimal sxBetAmount;         //视讯总投注
    private BigDecimal dzBetAmount;         //电子总投注
    private BigDecimal tyBetAmount;         //体育总投注
    private BigDecimal cpBetAmount;         //彩票总投注

    private BigDecimal totalLoseOrWin;      //总输赢
    private BigDecimal qpLoseOrWin;        //棋牌总输赢
    private BigDecimal byLoseOrWin;        //捕鱼总输赢
    private BigDecimal sxLoseOrWin;        //视讯总输赢
    private BigDecimal dzLoseOrWin;        //电子总输赢
    private BigDecimal tyLoseOrWin;         //体育总输赢
    private BigDecimal cpLoseOrWin;         //彩票总输赢

    private BigDecimal totalScore;          //总中奖
    private BigDecimal qpScore;             //棋牌总中奖
    private BigDecimal byScore;             //捕鱼总中奖
    private BigDecimal sxScore;             //视讯总中奖
    private BigDecimal dzScore;             //电子总中奖
    private BigDecimal tyScore;             //体育总中奖
    private BigDecimal cpScore;             //彩票总中奖

    private BigDecimal totalReward;         //总佣金
    private BigDecimal qpReward;           //棋牌总佣金
    private BigDecimal byReward;           //捕鱼总佣金
    private BigDecimal sxReward;           //视讯总佣金
    private BigDecimal dzReward;           //电子总佣金
    private BigDecimal tyReward;            //体育总佣金
    private BigDecimal cpReward;            //彩票总佣金

    private BigDecimal discount;      //彩金
    private BigDecimal payAmount;     //存款
    private BigDecimal withdrawAmount;      //取款
}
