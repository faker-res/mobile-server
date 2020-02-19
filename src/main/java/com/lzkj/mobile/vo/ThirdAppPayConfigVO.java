package com.lzkj.mobile.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * 第三方app充值配置
 */
@Data
public class ThirdAppPayConfigVO {

    private Integer id;
    /**
     * 支付类型:1易信
     */
    private Integer payType ;
    /**
     * 支付名称
     */
    private String payName;
    /**
     * 单笔下限金额
     */
    private BigDecimal payMin;
    /**
     * 代笔上限金额
     */
    private BigDecimal payMax;
    /**
     * 优惠比例
     */
    private BigDecimal promotionRate;
    /**
     * 可用状态：0关闭，1启用
     */
    private Integer enableState;

    private Integer agentId;

    private Integer sort;

    private String remark;

    private List<Integer> channelLevelId;

    private String imageUrl;
    private List<Integer> idList; // web前端用于批量操作

}
