package com.lzkj.mobile.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class SignAwardConfigItem {
    private Integer id;
    private Integer agentId;
    /**
     * 奖励上限
     */
    private BigDecimal amountMax;
    /**
     * 奖励下限
     */
    private BigDecimal amountMin;
    /**
     * RYTreasureDB.dbo.T_UserLevel.levelId
     */
    private Integer levelId;
    /**
     * 第N天的奖励
     */
    private Integer dayNum;
    /**
     * SignAwardConfig主键
     */
    private Integer configId;
}
