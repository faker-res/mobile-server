package com.lzkj.mobile.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class VipIntegrealConfigVO {
    private Integer vipLevel;
    private BigDecimal vipIntegral;
    private BigDecimal yearReward;
    private BigDecimal monthReward;
    private BigDecimal weekReward;
    private BigDecimal dayReward;
    private Integer agentId;
    private BigDecimal vipRankReward;
}
