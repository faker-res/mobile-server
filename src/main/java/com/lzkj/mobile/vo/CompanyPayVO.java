package com.lzkj.mobile.vo;

import lombok.Data;

import java.util.List;

@Data
public class CompanyPayVO {
    private Integer id;
    private Integer payId;
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
    private String remark;
    private List<Integer> channelLevelId;
}
