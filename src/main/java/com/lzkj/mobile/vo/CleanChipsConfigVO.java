package com.lzkj.mobile.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CleanChipsConfigVO {

    private Integer agentId;
    private int vipLevel;
    private BigDecimal betScore;
    private Boolean openType;
    private BigDecimal vipVersion;
    private BigDecimal vipElectron;
    private BigDecimal vipChess;
    private BigDecimal vipFish;
    private BigDecimal vipSport;
    private BigDecimal vipLottery;
    private BigDecimal betVersion;
    private BigDecimal betElectron;
    private BigDecimal betChess;
    private BigDecimal betFish;
    private BigDecimal betSport;
    private BigDecimal betLottery;

}
