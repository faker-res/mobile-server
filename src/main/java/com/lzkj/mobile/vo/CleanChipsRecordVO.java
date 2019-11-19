package com.lzkj.mobile.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CleanChipsRecordVO {
    private Integer userId;
    private String recordTime;
    private String kindName;
    private Integer kindId;
    private Integer kindType;
    private BigDecimal betScore;
    private BigDecimal ratio;
    private BigDecimal cleanValue;
}
