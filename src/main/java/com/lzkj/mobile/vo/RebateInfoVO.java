package com.lzkj.mobile.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class RebateInfoVO {

    private Integer memberCount;
    private String getTime;
    private BigDecimal reward;
    private Integer nullity;

    private BigDecimal betAmount;           //打码量
    private BigDecimal invalidBetAmount;    //打码量,默认传0
    private BigDecimal payAmount;           //存款
    private BigDecimal invalidPayAmount;    //存款数,默认传0
    private BigDecimal betReward;           //打码量佣金
    private BigDecimal payReward;           //存款佣金
    private String betTime;                 //打码量佣金领取时间
    private String payTime;                 //存款佣金领取时间
}
