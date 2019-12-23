package com.lzkj.mobile.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class TodayWinOrLoseVO {
    private BigDecimal todayProfit;//今日盈利
    private BigDecimal todayRecharge;//充值
    private BigDecimal todayWithDraw;//提现
    private BigDecimal betAmount;//投注
    private BigDecimal score;//中奖
    private BigDecimal giftScore;//赠送彩金
    private BigDecimal cashBack;//返水
    private BigDecimal recharge;//充值
    private BigDecimal withDraw;//提现
    private BigDecimal total;//汇总
}
