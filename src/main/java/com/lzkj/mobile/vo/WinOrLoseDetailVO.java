package com.lzkj.mobile.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class WinOrLoseDetailVO {
    private String kindName;
    private BigDecimal betAmount;
    private BigDecimal cashBack;
    private BigDecimal score;
    private BigDecimal profit;
}
