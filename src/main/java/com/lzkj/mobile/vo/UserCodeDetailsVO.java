package com.lzkj.mobile.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 玩家打码量详情
 */
@Data
public class UserCodeDetailsVO {
    private Integer userId;
    //业主号
    private Integer agentId;
    private Integer userLevel;
    /**时间*/
    private String applyDate;
    /**需求打码量*/
    private BigDecimal inAmounts;
    /**实际打码量*/
    private BigDecimal codeAmountCount;
    /**待完成的打码量*/
    private BigDecimal needAmountCount;
    /**打码类型*/
    private Integer status;
    /**打码类型名称*/
    private String typeName;
    /**是否可提现：-1表示空；0表示不可提现；1表示可提现*/
    private Integer flag =-1;
}
