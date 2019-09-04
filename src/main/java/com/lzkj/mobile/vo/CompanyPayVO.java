package com.lzkj.mobile.vo;

import lombok.Data;

@Data
public class CompanyPayVO {
    private Integer id;
    private String payType;
    private String payTypeName;
    private String userName;
    private String userAccount;
    private String bankName;
    private String bankAddr;
    private Short isOpen;
    private Integer sort;
    private Integer agentId;
    private String imageUrl;
    private String limitRecord;
}
