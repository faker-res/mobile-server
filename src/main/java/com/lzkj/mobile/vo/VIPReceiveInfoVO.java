package com.lzkj.mobile.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class VIPReceiveInfoVO {
    private Integer userId;
    private BigDecimal receiveScore;//领取金币
    private String receiveDate;     //领取时间
    private Integer vipLevel;
    private short receiveLenvel;
    private String rewardType;
}
