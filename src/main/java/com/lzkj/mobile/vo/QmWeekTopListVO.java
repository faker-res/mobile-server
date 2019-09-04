package com.lzkj.mobile.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class QmWeekTopListVO {
    private String nickName;
    private int userID;
    private BigDecimal reward;
    private String updateDate;

}
