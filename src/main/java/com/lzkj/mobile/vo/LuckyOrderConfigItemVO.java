package com.lzkj.mobile.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 幸运注单配置 幸运号码项
 */
@Data
public class LuckyOrderConfigItemVO {
    private Integer itemId;
    /**
     * LuckyOrderConfig主键
     */
    private Integer configId;
    /**
     * 幸运注单号码
     */
    private Integer orderNumber;
    /**
     * 奖励类型：1固定金额，2投注金额倍数，3打码金额倍数
     */
    private Integer awardType;
    /**
     * 奖金（当AwardType为固定金额时适用）
     */
    private BigDecimal awardAmount;
    /**
     * 倍数（当AwardType为倍数时适用）
     */
    private Integer awardMultiple;
    /**
     * 可用状态：0关闭，1启用
     */
    private Integer enableState;
}
