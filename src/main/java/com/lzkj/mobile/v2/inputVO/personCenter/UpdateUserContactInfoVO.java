package com.lzkj.mobile.v2.inputVO.personCenter;

import com.lzkj.mobile.v2.inputVO.BaseVO;
import com.lzkj.mobile.v2.util.ValidateParamUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 *  * @Description: TODO   
 *  * @CreateDate:  2020/3/2 15:57  
 */
@Data
@ApiModel(description = "修改用户信息数据封装类")
public class UpdateUserContactInfoVO extends BaseVO {

    @ApiModelProperty(value = "手机号")
    @NotNull
    @Pattern(regexp = ValidateParamUtil.Regexp.PHONE)
    private String mobilePhone;

    @ApiModelProperty(value = "QQ")
    @NotNull
    private String qq;

    @ApiModelProperty(name = "eMail", value = "Email")
    @NotNull
    @Email
    private String eMail;

    @ApiModelProperty(value = "用户ID", required = true)
    @NotNull
    @Length(min = 1)
    private Integer userId;

    @ApiModelProperty(value = "业主ID", required = true)
    @NotNull
    @Length(min = 1)
    private Integer agentId;

}
