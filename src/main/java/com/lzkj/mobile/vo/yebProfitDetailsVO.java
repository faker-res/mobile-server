package com.lzkj.mobile.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class yebProfitDetailsVO {
   private BigDecimal profitAmount;   //收益金额
   private BigDecimal profitRate;     //万份收益率
   private String insertDate;     //收益日期
   private BigDecimal noSureAmount;   //未确认份额
   private BigDecimal sureAmount;     //已确认份额
   private BigDecimal beforeAmount;   //原余额宝总额
   private BigDecimal afterAmount;    //收益后余额宝总额
   private BigDecimal InsureScore; //银行金币
   private BigDecimal YesterdayAfter;//昨日收益
   private BigDecimal TotalAfter;//总收益
}
