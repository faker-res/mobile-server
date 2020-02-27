package com.lzkj.mobile.v2.returnVO.bank;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 *    
 *  *  
 *  * @Project: data-server 
 *  * @Package: com.lzkj.fund.model.vo 
 *  * @Description: TODO   
 *  * @Author:   horus   
 *  * @CreateDate:  2020/2/5 10:50  
 *  * @Version:   v1.0
 *  *    
 *  
 */
@Data
@ApiModel(value = "BankInfoVO", description = "银行初始化配置数据封装类")
public class BankAgentVO {

    @ApiModelProperty(name = "id", value = "主键ID")
    private Integer id;

    @ApiModelProperty(name = "bankCode", value = "银行编码")
    private String bankCode;

    @ApiModelProperty(name = "bankName", value = "银行名称")
    private String bankName;

    @ApiModelProperty(name = "creator", value = "创建人")
    private String creator;

    @ApiModelProperty(name = "createTime", value = "创建时间")
    private Date createTime;

    @ApiModelProperty(name = "status", value = "0：开启 1：关闭")
    private Integer status;

    @ApiModelProperty(name = "sort", value = "排序")
    private Integer sort;

}
