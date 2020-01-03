package com.lzkj.mobile.vo;

import lombok.Data;

@Data
public class IndividualDatumVO {
    private Integer userId;
    private Integer gameId;
    private Integer agentId;
    private String compellation; //开户姓名 实名
    private String bankNO;
    private String bankName;
    private String bankAddress;
    private String alipayRealName;
    private String seatPhone;
    private String agentAcc;
    private Integer status;
    private String remarks;
    private String collectDate;
    private Integer bankStatus;
}
