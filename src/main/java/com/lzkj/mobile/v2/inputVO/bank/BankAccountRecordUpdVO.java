package com.lzkj.mobile.v2.inputVO.bank;

import com.lzkj.mobile.v2.inputVO.BaseVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 *    
 *  *  
 *  * @Project: data-server 
 *  * @Package: com.lzkj.fund.model.inputVO 
 *  * @Description: TODO   
 *  * @Author:   horus   
 *  * @CreateDate:  2020/2/5 13:54  
 *  * @Version:   v1.0
 *  *    
 *  
 */
@Data
@ApiModel(value = "BankAccountRecordUpdVO", description = "用户银行卡修改封装类")
public class BankAccountRecordUpdVO extends BaseVO {

    @ApiModelProperty(name = "id", value = "主键ID", hidden = true)
    private Integer id;

    @ApiModelProperty(name = "recordId", value = "主键ID")
    @NotNull(message = "recordId不能为空")
    private Integer recordId;

    @ApiModelProperty(name = "status", value = "要变更的状态 3 更换成功 4 拒绝申请 5 取消审核", allowableValues = "3,4,5", hidden = true)
    private Integer status;

}
