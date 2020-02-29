package com.lzkj.mobile.v2.inputVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.bouncycastle.util.Integers;
import org.hibernate.validator.constraints.Length;

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
public class BaseGameIdVO extends BaseVO{

    @ApiModelProperty(value = "游戏ID")
    @NotNull
    @Length(min = 1)
    private Integer gameId;


}
