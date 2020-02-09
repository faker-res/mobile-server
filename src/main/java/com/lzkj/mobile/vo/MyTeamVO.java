package com.lzkj.mobile.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class MyTeamVO {

    private Integer userId;
    private Integer gameId;
    private BigDecimal score;
    private String betTime;
    private String gameName;

    private Integer nullity;
    private Integer beatAmount;

    private Integer loseOrWin;
    private BigDecimal backWater;
    private BigDecimal discount;

}
