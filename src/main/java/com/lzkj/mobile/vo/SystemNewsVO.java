package com.lzkj.mobile.vo;

import lombok.Data;

@Data
public class SystemNewsVO {
    private Integer id;
    private Integer agentId;
    private String agentAcc;
    private Short newsLevel;
    private String body;
    private String beginDate;
    private String endDate;
    private String modifyTime;
}
