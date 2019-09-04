package com.lzkj.mobile.vo;

import lombok.Data;

@Data
public class BindPhoneVO {
    private Integer userId;
    private String password;
    private String phone;
    private String verifyCode;
    private String realName;
    private String bankNo;
    private String bankName;
}
