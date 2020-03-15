package com.lzkj.mobile.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ShareDetailInfoVO {
    private int detailId;
    private int operUserId;
    private int shareId;
    private int userId;
    private int gameId;
    private String accounts;
    private int cardTypeId;
    private String  serialId;
    private String orderId;
    private BigDecimal orderAmount;
    private BigDecimal discountScale;
    private BigDecimal payAmount;
    private BigDecimal currency;
    private BigDecimal beforeCurrency;
    private String ipAddress;
    private String applyDate;
    private BigDecimal  gold;
    private BigDecimal  beforeGold;
    private int roomCard;
    private int beforeRoomCard;
    private boolean aUShow;
    private String merchantOrderId;//商户订单号
    private BigDecimal insureScore;
    private Integer vipLevel;

    private BigDecimal score;
}
