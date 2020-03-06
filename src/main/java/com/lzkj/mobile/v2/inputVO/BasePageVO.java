package com.lzkj.mobile.v2.inputVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 *    
 *  *  
 *  * @Project: data-server 
 *  * @Package: com.lzkj.fund.model 
 *  * @Description: TODO   
 *  * @Author:      
 *  * @CreateDate:  2020/2/5 10:47  
 *  * @Version:   v1.0
 *  *    
 *  
 */
@Data
public class BasePageVO extends BaseVO{

    @ApiModelProperty(name = "pageNo", value = "页码", required = true)
    @NotNull(message = "pageIndex不能为空")
    private Integer pageIndex;

    @ApiModelProperty(name = "pageSize", value = "页面大小", required = true)
    @NotNull(message = "pageSize不能为空")
    private Integer pageSize;

}
