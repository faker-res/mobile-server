package com.lzkj.mobile.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class QmPromotionListVO {
    private String date;
    private int memberCount;
    private BigDecimal rewardCount;
}
