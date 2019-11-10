package com.lzkj.mobile.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class AccountChangeStatisticsVO {
    private BigDecimal recharge;  //充值
    private BigDecimal offer;  //优惠
    private BigDecimal balance;  //余额
    private BigDecimal withdraw;  //提现
    private BigDecimal backwater;  //返水

}
