package com.lzkj.mobile.v2.inputVO.bank;

import com.lzkj.mobile.v2.inputVO.BaseVO;
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
 *  * @CreateDate:  2020/2/8 16:49  
 *  * @Version:   v1.0
 *  *    
 *  
 */
@Data
@ApiModel(value = "CanChangeBankCardVO", description = "是否能更换银行卡数据封装类")
public class CanChangeBankCardVO extends BaseVO {

    @ApiModelProperty(name = "gameId", value = "所属用户游戏ID")
    @NotNull(message = "gameId不能为空")
    private Integer gameId;

}
