package com.lzkj.mobile.v2.inputVO.bank;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 *    
 *  *  
 *  * @Project: agent 
 *  * @Package: com.lzkj.agent.v2.model.inputVO.bank 
 *  * @Description: TODO   
 *  * @Author:   horus   
 *  * @CreateDate:  2020/2/5 18:19  
 *  * @Version:   v1.0
 *  *    
 *  
 */
@Data
@ApiModel(value = "BankAccountPageVO", description = "用户银行卡分页数据封装类")
public class BankAccountPageVO {

    @ApiModelProperty(name = "pageNo", value = "页码", hidden = true)
    private Integer pageNo;

    @ApiModelProperty(name = "pageSize", value = "页面大小", hidden = true)
    private Integer pageSize;

    @ApiModelProperty(name = "gameId", value = "用户游戏ID")
    private String gameId;

    @ApiModelProperty(name = "isWeb", value = "是否是WEB端", hidden = true)
    private boolean isWeb;


}
