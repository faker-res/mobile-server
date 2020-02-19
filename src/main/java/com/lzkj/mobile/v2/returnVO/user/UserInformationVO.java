package com.lzkj.mobile.v2.returnVO.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "UserInformationVO", description = "游戏用户基本信息返回封装类")
public class UserInformationVO {

	@ApiModelProperty(value = "账户")
	private String accounts;

	@ApiModelProperty(value = "昵称/真名")
	private String nickName;

	@ApiModelProperty(value = "性别")
	private Integer gender;

	@ApiModelProperty(value = "注册手机号")
	private String mobilePhone;

	@ApiModelProperty(value = "QQ")
	private String qq;

	@ApiModelProperty(value = "Email")
	private String email;
}
