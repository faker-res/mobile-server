package com.lzkj.mobile.v2.inputVO.bank;

import com.lzkj.mobile.v2.inputVO.BaseVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

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
public class BankAccountPageVO extends BaseVO {

    @ApiModelProperty(value = "页码", hidden = true)
    private Integer pageNo;

    @ApiModelProperty(value = "页面大小", hidden = true)
    private Integer pageSize;

    @ApiModelProperty(value = "用户游戏ID")
    @NotNull
    @Length(min = 1)
    private String gameId;

    @ApiModelProperty(value = "是否是WEB端", hidden = true)
    private boolean isWeb;


}
