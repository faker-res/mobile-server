package com.lzkj.mobile.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 玩家打码量详情
 */
@Data
public class UserCodeDetailsVO {
    private Integer userId;
    /**业主号*/
    private Integer agentId;
    private Integer userLevel;
    /**时间*/
    private String applyDate;
    /**需求打码量*/
    private BigDecimal inAmounts;
    /**实际打码量*/
    private BigDecimal codeAmountCount;
    /**状态*/
    private Integer status;

}
