package com.lzkj.mobile.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class AccountsQmRatioVO {
    private Integer userId;
    private Integer spreaderId;
    private Boolean zz_IsAgent;
    private BigDecimal versionRatio;
    private BigDecimal electronRatio;
    private BigDecimal chessRatio;
    private BigDecimal fishRatio;
    private BigDecimal sportRatio;
    private BigDecimal lotteryRatio;
    private Integer agentId;
}
