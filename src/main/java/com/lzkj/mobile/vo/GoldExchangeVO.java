package com.lzkj.mobile.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class GoldExchangeVO {
    private Integer id;
    private Boolean isOpen;
    private Boolean isSale;
    private BigDecimal gameCnt;
    private Integer activeCnt;
    private Integer backCnt;
    private Integer chargeCnt;
    private Integer systemCnt;
    private Integer weekOpenDay;
    private Short shour;
    private Short ehour;
    private Integer dailyApplyTimes;
    private Double balancePrice;
    private Double minBalance;
    private Double counterFee;
    private Double minCounterFee;
    private BigDecimal minPlayerScore;
    private Short drawMultiple;
    private Boolean isMobileSale;
    private Integer agentId;
    private Integer bankType;
    private BigDecimal bankCounterFee;
    private BigDecimal minBankCounterFee;
    private Integer bankTimes;
    private Integer isOpenAli;
    private Integer isOpenBank;
}
