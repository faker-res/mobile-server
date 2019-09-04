package com.lzkj.mobile.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class QmUserRewardVO {
    private int userID;
    private BigDecimal allReward;
    private BigDecimal notLiquidationReward;
}
