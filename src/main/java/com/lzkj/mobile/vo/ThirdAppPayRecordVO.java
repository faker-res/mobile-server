package com.lzkj.mobile.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 第三方app充值记录
 *
 * @author xxx
 */
@Data
public class ThirdAppPayRecordVO {

    private Integer id;

    private Integer gameId;

    /**
     * 支付类型（来自配置）
     */
    private Integer payType;

    /**
     * 支付名称（来自配置）
     */
    private String payName;

    private String orderNum	;

    private BigDecimal orderAmount;

    private BigDecimal realAmount;

    private Long createTime;

    /**
     * 操作员帐号
     */
    private String operator;

    /**
     * 支付状态：1-未支付 2-支付成功 3-支付失败
     */
    private Integer payState;

    private String remark;

    /**
     * 充值信息（用户在客户端填写的预留信息）
     */
    private String reserveMsg;

    private Integer agentId;

    private String userAccount;

    private Integer userId;

    private Long updateTime;

}
