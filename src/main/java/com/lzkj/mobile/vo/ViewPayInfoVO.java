package com.lzkj.mobile.vo;

import lombok.Data;

@Data
public class ViewPayInfoVO {
    private int id;

    private String qudaoName;

    private String type;
    private int shareId;
    private String payTypeCode;
    private String payType;
    private int qudaoID;
    private String memberId;
    private String memberKey;
    private String sendUrl;
    private String appId;
    private short payMode;

}
