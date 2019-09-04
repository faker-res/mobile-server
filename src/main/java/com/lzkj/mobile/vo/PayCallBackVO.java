package com.lzkj.mobile.vo;

import lombok.Data;

@Data
public class PayCallBackVO {
    private String orderId;
    private String ownerOrderId;
    private String orderType;
    private String amount;
    private String ownerId;
    private String ownerSign;

}
