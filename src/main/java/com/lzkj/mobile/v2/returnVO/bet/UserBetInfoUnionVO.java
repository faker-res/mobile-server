package com.lzkj.mobile.v2.returnVO.bet;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@ApiModel(description = "用户打码量数据统计封装类")
public class UserBetInfoUnionVO {

    @ApiModelProperty(value = "需求总打码量")
    private BigDecimal totalNeedsBet;

    @ApiModelProperty(value = "实际总打码量")
    private BigDecimal totalFactBet;

    @ApiModelProperty(value = "未完成总打码量")
    private BigDecimal totalNoFinishBet;

    @ApiModelProperty(value = "用户打码量详情")
    private List<UserBetInfoVO> UserBetInfoList;

}
