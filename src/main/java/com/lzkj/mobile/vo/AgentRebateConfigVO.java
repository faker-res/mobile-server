package com.lzkj.mobile.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class AgentRebateConfigVO {
    private BigDecimal compenyRebate = BigDecimal.ZERO;  //公司返现比例
    private BigDecimal silverRebate = BigDecimal.ZERO;  //银商返现比例
    private Double rebate;//活动返现比例


}
