package com.lzkj.mobile.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CleanChipsVO {
    private String kindName;//游戏

    private BigDecimal cleanBet; //洗码量（打码量）

    private BigDecimal ratio; //洗码比列

    private BigDecimal cleanValue;//洗码金额

}
