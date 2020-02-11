package com.lzkj.mobile.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CompanyOnlineOrderVO {
    /**
     * 业主id
     */
    private Integer agentId;
    /**
     * 玩家唯一的标识
     */
    private Integer userId;
    /**
     * 玩家唯一的标识
     */
    private Integer gameId;
    /**
     * 玩家选择的公司支付的id
     */
    private Integer payId;
    /**
     * 订单金额
     */
    private BigDecimal orderAmount;
    /**
     * 支付时的备注
     */
    private String remarks;
    /**
     * 玩家账号
     */
    private String account;
}
