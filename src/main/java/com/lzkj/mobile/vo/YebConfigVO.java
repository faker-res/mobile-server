package com.lzkj.mobile.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class YebConfigVO {
    private short isOpen;    //是否开启   0关闭 1开启   默认0
    private String description ="";     //余额宝说明
}
