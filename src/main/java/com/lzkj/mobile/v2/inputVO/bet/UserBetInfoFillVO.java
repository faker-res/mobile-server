package com.lzkj.mobile.v2.inputVO.bet;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
@ApiModel(description = "填充用户实际打码量入参数据封装类")
public class UserBetInfoFillVO {

    @ApiModelProperty(value = "用户游戏ID")
    private Integer gameId;

    @ApiModelProperty(value = "用户ID")
    private Integer userId;

    @ApiModelProperty(value = "实际打码量", required = true)
    private BigDecimal betFact;


}
