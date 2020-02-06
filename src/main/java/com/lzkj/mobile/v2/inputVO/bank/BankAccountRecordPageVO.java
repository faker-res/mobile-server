package com.lzkj.mobile.v2.inputVO.bank;

import com.lzkj.mobile.v2.inputVO.BasePageVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 *    
 *  *  
 *  * @Project: data-server 
 *  * @Package: com.lzkj.fund.model.vo.input 
 *  * @Description: TODO   
 *  * @Author:   horus   
 *  * @CreateDate:  2020/2/5 10:56  
 *  * @Version:   v1.0
 *  *    
 *  
 */
@Data
@ApiModel(value = "BankAccountPageVO", description = "用户银行卡绑定记录分页数据封装类")
public class BankAccountRecordPageVO extends BasePageVO {

    @ApiModelProperty(name = "gameId", value = "用户游戏ID")
    @NotNull(message = "gameId不能为空")
    private String gameId;


}
