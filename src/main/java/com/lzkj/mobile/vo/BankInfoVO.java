package com.lzkj.mobile.vo;

import lombok.Data;

@Data
public class BankInfoVO {
    private Integer id;
    private Integer agentId;
    private String bankCode;
    private String bankName;
    private Boolean nullity;
    private Integer sort;
}
