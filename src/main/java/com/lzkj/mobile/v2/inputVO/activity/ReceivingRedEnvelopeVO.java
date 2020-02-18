package com.lzkj.mobile.v2.inputVO.activity;

import com.lzkj.mobile.v2.inputVO.BaseVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 *    
 *  *  
 *  * @Project: data-server 
 *  * @Package: com.lzkj.accounts.v2.model.vo.inputVO.mobile 
 *  * @Description: TODO   
 *  * @Author:   horus   
 *  * @CreateDate:  2020/2/18 16:45  
 *  * @Version:   v1.0
 *  *    
 *  
 */
@Data
@ApiModel(description = "抢红包入参")
public class ReceivingRedEnvelopeVO extends BaseVO {

    @ApiModelProperty(value = "用户ID")
    @NotNull(message = "用户不能为空")
    private Integer userId;

    @ApiModelProperty(value = "额度")
    private BigDecimal score;

    @ApiModelProperty(value = "机器")
    private String machineId;

    @ApiModelProperty(value = "类型ID")
    private Integer typeId;

    @ApiModelProperty(value = "活动ID")
    private Integer activityId;

    @ApiModelProperty(value = "IP", hidden = true)
    private String ip;

}
