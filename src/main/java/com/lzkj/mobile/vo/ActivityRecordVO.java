package com.lzkj.mobile.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ActivityRecordVO {
    private Integer id;
    private Integer activityId;
    private String title;
    private Integer userId;
    private String applyDate;
    private String collectDate;
    private Integer status;
    private String memo;
    private BigDecimal amount;
}
