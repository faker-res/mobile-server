package com.lzkj.mobile.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class UserCodeDetailsVO {
    private Integer userId;
    private Integer agentId;
    private Integer userLevel;
    private String applyDate;  //时间
    private BigDecimal inAmounts;  //需求打码量
    private BigDecimal codeAmountCount; //实际打码量
    private Integer status;  //状态
}
