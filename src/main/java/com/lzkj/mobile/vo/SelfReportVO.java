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

    private BigDecimal totalBackWater;         //总返水
    private BigDecimal qpBackWater;           //棋牌总返水
    private BigDecimal byBackWater;           //捕鱼总返水
    private BigDecimal sxBackWater;           //视讯总返水
    private BigDecimal dzBackWater;           //电子总返水
    private BigDecimal tyBackWater;            //体育总返水
    private BigDecimal cpBackWater;            //彩票总返水

    private BigDecimal discount;      //彩金
    private BigDecimal payAmount;     //存款
    private BigDecimal withdrawAmount;      //取款
}
