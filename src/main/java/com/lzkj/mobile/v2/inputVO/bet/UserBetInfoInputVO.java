package com.lzkj.mobile.v2.inputVO.bet;

import com.lzkj.mobile.v2.inputVO.BaseVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "查询用户打码量入参数据封装类")
public class UserBetInfoInputVO extends BaseVO {

    @ApiModelProperty(value = "用户游戏ID")
    private Integer gameId;

    @ApiModelProperty(value = "用户ID")
    private Integer userId;
}
