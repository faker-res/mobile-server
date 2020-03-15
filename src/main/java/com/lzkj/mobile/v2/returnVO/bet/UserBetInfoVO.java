package com.lzkj.mobile.v2.returnVO.bet;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@ApiModel(description = "用户打码量数据封装类")
public class UserBetInfoVO {

    @ApiModelProperty(value = "主键ID")
    private Integer id;

    @ApiModelProperty(value = "所属用户ID")
    private Integer userId;

    @ApiModelProperty(value = "用户游戏ID")
    private String gameId;

    @ApiModelProperty(value = "业主ID")
    private Integer agentId;

    @ApiModelProperty(value = "金额")
    private BigDecimal amount;

    @ApiModelProperty(value = "需求打码量")
    private BigDecimal betNeeds;

    @ApiModelProperty(value = "实际打码量")
    private BigDecimal betFact;

    @ApiModelProperty(value = "打码量类型编码")
    private String betType;

    @ApiModelProperty(value = "打码量类型名称")
    private String betTypeName;

    @ApiModelProperty(value = "状态  0:未完成 1:已完成")
    private Integer betStatus;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

}
