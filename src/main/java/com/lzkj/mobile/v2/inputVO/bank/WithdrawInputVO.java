package com.lzkj.mobile.v2.inputVO.bank;

import com.lzkj.mobile.v2.inputVO.BaseVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
@ApiModel(description = "用户提现数据封装类")
public class WithdrawInputVO extends BaseVO {

    @ApiModelProperty(value = "游戏ID", required = true)
    @NotNull
    @Length(min = 1)
    private Integer gameId;

    @ApiModelProperty(value = "类型(0：alipay 1：bank)", required = true, allowableValues = "1,0")
    @NotNull
    private Integer type;

    @ApiModelProperty(value = "兑换金额", required = true)
    @NotNull
    @Length(min = 1)
    private BigDecimal score;

    @ApiModelProperty(value = "银行卡主键ID", required = true)
    @NotNull
    @Length(min = 1)
    private Integer id;

    @ApiModelProperty(value = "申请IP", hidden = true)
    private String ip;

    @ApiModelProperty(value = "兑换订单号", required = true)
    @NotBlank
    @Length(min = 1)
    private String orderId;

    @ApiModelProperty(value = "安全密码")
    private String insurePass;

    @ApiModelProperty(value = "动态密码", required = true)
    @NotBlank
    private String dynamicPass;


}
