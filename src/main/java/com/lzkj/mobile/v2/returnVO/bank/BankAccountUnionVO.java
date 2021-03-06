package com.lzkj.mobile.v2.returnVO.bank;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 *    
 *  *  
 *  * @Project: data-server 
 *  * @Package: com.lzkj.fund.model.vo 
 *  * @Description: TODO   
 *  * @Author:      
 *  * @CreateDate:  2020/2/5 10:50  
 *  * @Version:   v1.0
 *  *    
 *  
 */
@Data
@ApiModel(description = "用户可用银行卡信息数据封装类")
public class BankAccountUnionVO {

    @ApiModelProperty(value = "提款限额")
    private BigDecimal attributes;

    @ApiModelProperty(value = "未完成打码量")
    private BigDecimal betAmount;

    @ApiModelProperty(value = "可用银行卡信息")
    private List<BankAccountVO> list;

    @ApiModelProperty(value = "无可用银行卡和待审核的银行卡")
    private boolean noCard;

}
