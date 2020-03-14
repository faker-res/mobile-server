package com.lzkj.mobile.v2.inputVO.bet;

import com.lzkj.mobile.v2.inputVO.BasePageVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

@Data
@ApiModel(description = "查询用户打码量入参数据封装类")
public class UserBetInfoInputVO extends BasePageVO {

    @ApiModelProperty(value = "用户游戏ID")
    @NotNull
    @Length(min = 1)
    private Integer gameId;

    @ApiModelProperty(value = "状态(0:未完成 1:已完成)", allowableValues = "0,1")
    @NotNull
    private Integer betStatus;

    @ApiModelProperty(value = "终端", hidden = true)
    private boolean isWeb;

}
