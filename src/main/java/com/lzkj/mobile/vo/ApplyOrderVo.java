package com.lzkj.mobile.vo;

import lombok.Data;

@Data
public class ApplyOrderVo {

    private String orderId;

    private double sellMoney;

    private String rejectReason;

    private int status;

    private String applyDate;


}
