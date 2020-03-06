package com.lzkj.mobile.vo;

import com.lzkj.mobile.v2.util.ValidateParamUtil;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
public class BindPhoneVO {
    private Integer userId;

    @NotBlank
    @Length(min = 6)
    private String password;

    @NotBlank
    @Pattern(regexp = ValidateParamUtil.Regexp.PHONE)
    private String phone;

    @NotBlank
    private String verifyCode;
    private String realName;

    @Length(min = 15, max = 20)
    private String bankNo;
    private String bankName;
}
