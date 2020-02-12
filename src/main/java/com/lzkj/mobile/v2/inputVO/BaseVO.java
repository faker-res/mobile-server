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
 *  * @Author:   horus   
 *  * @CreateDate:  2020/2/5 10:47  
 *  * @Version:   v1.0
 *  *    
 *  
 */
@Data
public class BaseVO {

    @ApiModelProperty(value = "签名")
    private String s;


}
