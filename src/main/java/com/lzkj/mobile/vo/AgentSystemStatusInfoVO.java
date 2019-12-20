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
    private Integer goldGiftCount;//展示赠送金币数量
    private Integer optionButton;//0-不选；1-账号；2-手机号；3-账号和手机号；默认0
}
