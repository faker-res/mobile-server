package com.lzkj.mobile.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class QmLiquidationRewardRecordVO {
    private int userID;
    private BigDecimal liquidationReward;
    private String createDate;
    private int status;
    private String statusWord;
}
