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
 *  * @Project: data-server 
 *  * @Package: com.lzkj.fund.model.vo.input 
 *  * @Description: TODO   
 *  * @Author:      
 *  * @CreateDate:  2020/2/5 10:56  
 *  * @Version:   v1.0
 *  *    
 *  
 */
@Data
@ApiModel(description = "修改用户银行卡信息分页数据封装类")
public class BankAccountUpdVO extends BaseVO {

    public final static int TYPE_ONE = 1;

    @ApiModelProperty(value = "ID主键")
    @NotNull
    @Length(min = 1)
    private Integer id;

    @ApiModelProperty(value = "是否为提款卡 0：否 1：是", hidden = true)
    private Integer isUse;

    @ApiModelProperty( value = "类型 1：修改提款卡", hidden = true)
    private Integer type;

}
