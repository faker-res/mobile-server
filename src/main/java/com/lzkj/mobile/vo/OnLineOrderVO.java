package com.lzkj.mobile.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OnLineOrderVO {

    private int onLineId;

    private int operUserId;

    private int shareId;

    private int userId;

    private int gameId;

    private String accounts;

    private String orderId;

    private BigDecimal orderAmount;

    private BigDecimal discountScale;

    private BigDecimal payAmount;

    private int rate;

    private BigDecimal currency;

    private short orderStatus;

    private String iPAddress;

    private String applyDate;

    private short curType;

    private int orderType;
    
    private String payType;
    
    private int payInfoId;
}
