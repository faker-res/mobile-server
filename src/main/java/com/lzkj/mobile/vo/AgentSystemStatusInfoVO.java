package com.lzkj.mobile.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class AgentSystemStatusInfoVO {
    private Integer id;
    private String statusName;
    private BigDecimal statusValue;
    private String statusString;
    private String statusTip;
    private String statusDescription;
    private short isShow;
    private Integer goldGiftIconOpen;
    private Integer goldGiftCount;
}
