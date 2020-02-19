package com.lzkj.mobile.v2.inputVO.bank;

import com.lzkj.mobile.v2.inputVO.BaseVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

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
@ApiModel(description = "修改用户银行卡信息分页数据封装类")
public class BankAccountUpdVO extends BaseVO {

    public final static int TYPE_ONE = 1;

    @ApiModelProperty(name = "id", value = "ID主键")
    private Integer id;

    @ApiModelProperty(name = "isUse", value = "是否为提款卡 0：否 1：是", hidden = true)
    private Integer isUse;

    @ApiModelProperty(name = "type", value = "类型 1：修改提款卡", hidden = true)
    private Integer type;

}
