package com.lzkj.mobile.v2.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 *    
 *  *  
 *  * @Project: data-server 
 *  * @Package: com.lzkj.accounts.v2.model.dto 
 *  * @Description: TODO   
 *  * @Author:      
 *  * @CreateDate:  2020/2/12 21:27  
 *  * @Version:   v1.0
 *  *    
 *  
 */
@Data
@ApiModel(description = "发送邮件数据传输类")
public class InternalMessageDto {

    @ApiModelProperty(value = "发送邮件类型")
    private int type;

    @ApiModelProperty(value = "时间，值对应时间占位符，为空时默认填充yyyy-MM-dd HH:mm:ss")
    private String time;

    @ApiModelProperty(value = "充值金额，值对应充值金额占位符")
    private BigDecimal recharge;

    @ApiModelProperty(value = "提现金额，值对应提现金额占位符")
    private BigDecimal withdrawal;

    @ApiModelProperty(value = "奖励金额，值对应奖励金额占位符")
    private BigDecimal award;

    @ApiModelProperty(value = "返利金额，值对应返利金额占位符")
    private BigDecimal cashBack;

    @ApiModelProperty(value = "银行卡尾号（后四位数），值对应银行卡尾号（后四位数）占位符")
    private String suffixBankNo;

    @ApiModelProperty(value = "所属用户ID")
    private Integer userId;

    @ApiModelProperty(value = "所属用户游戏ID")
    private Integer gameId;

    @ApiModelProperty(value = "模板编码")
    private String code;

    @ApiModelProperty(value = "标题，模板编码优先")
    private String subject;

    @ApiModelProperty(value = "内容，模板编码优先")
    private String content;

    @ApiModelProperty(value = "创建人，为空则取值SYSTEM")
    private String operator;
}
