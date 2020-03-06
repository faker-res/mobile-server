package com.lzkj.mobile.v2.inputVO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 *    
 *  *  
 *  * @Project: mobile-server 
 *  * @Package: com.lzkj.mobile.v2.inputVO 
 *  * @Description: TODO   
 *  * @Author:      
 *  * @CreateDate:  2020/2/17 10:53  
 *  * @Version:   v1.0
 *  *    
 *  
 */
@Data
@ApiModel(description = "用户账户明细分页数据封装类")
public class AccountsDetailPageVO extends BasePageVO{

    @ApiModelProperty(value = "用户ID", required = true)
    @NotNull
    @Length(min = 1)
    private Integer userId;

    @ApiModelProperty(value = "游戏状态", required = true)
    @NotNull
    private Integer typeId;

    @ApiModelProperty(value = "日期类型", required = true)
    @NotNull
    private Integer date;

}
