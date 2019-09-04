package com.lzkj.mobile.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Data
public class QmRewardRecordVO {
    private int SpreaderID;
    private BigDecimal Reward;
    private int UserID;
    private BigDecimal Revenue;
    private Double Rate;
    private Timestamp CreateDate;
    private short isDirect;
}
