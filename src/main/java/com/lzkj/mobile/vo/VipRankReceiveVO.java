package com.lzkj.mobile.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class VipRankReceiveVO {
    private Integer userId;
    private Integer vipRank;
    private Boolean nullity;
    private BigDecimal rankMoney;
    private String receiveDate;
}
