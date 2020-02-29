package com.lzkj.mobile.v2.inputVO.activity;

import com.lzkj.mobile.v2.inputVO.BaseVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 *    
 *  *  
 *  * @Project: data-server 
 *  * @Package: com.lzkj.accounts.v2.model.vo.inputVO.mobile 
 *  * @Description: TODO   
 *  * @Author:      
 *  * @CreateDate:  2020/2/18 16:45  
 *  * @Version:   v1.0
 *  *    
 *  
 */
@Data
@ApiModel(description = "红包雨入参")
public class ReceivingRedEnvelopeRainVO extends BaseVO {

    @ApiModelProperty(value = "ID")
    private Integer id;

    @ApiModelProperty(value = "用户ID")
    private Integer userId;

    @ApiModelProperty(value = "机器")
    private String machineId;

    @ApiModelProperty(value = "IP", hidden = true)
    private String ip;

}
