package com.lzkj.mobile.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class AccountsLevelVO {
    private Integer userId;
    private Integer gameId;
    private Integer vipLevel;
    private BigDecimal vipIntegral;
    private Integer parentId;
    private Short faceId;
    private Short gender;
}
