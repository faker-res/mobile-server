package com.lzkj.mobile.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class GuaranteedRebatesVO {
    private Integer gameId;
    private BigDecimal ratio;
    private int kindType;
}
