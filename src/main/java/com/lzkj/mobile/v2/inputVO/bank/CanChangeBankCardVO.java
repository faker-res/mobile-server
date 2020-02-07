package com.lzkj.mobile.v2.inputVO.bank;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 *    
 *  *  
 *  * @Project: mobile-server 
 *  * @Package: com.lzkj.mobile.v2.inputVO.bank 
 *  * @Description: TODO   
 *  * @Author:   horus   
 *  * @CreateDate:  2020/2/7 21:30  
 *  * @Version:   v1.0
 *  *    
 *  
 */
@Data
@ApiModel(value = "CanChangeBankCardVO", description = "是否可以修改银行卡数据封装类")
public class CanChangeBankCardVO {

    @ApiModelProperty(name = "gameId", value = "游戏ID")
    @NotNull(message = "gameId不能为空")
    private Integer gameId;

}
