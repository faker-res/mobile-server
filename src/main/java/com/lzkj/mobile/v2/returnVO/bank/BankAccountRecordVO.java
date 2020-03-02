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
 *  * @Author:      
 *  * @CreateDate:  2020/2/5 10:50  
 *  * @Version:   v1.0
 *  *    
 *  
 */
@Data
@ApiModel(description = "用户银行卡申请记录数据封装类")
public class BankAccountRecordVO {

    @ApiModelProperty(name = "id", value = "主键ID")
    private Integer id;

    @ApiModelProperty(name = "userId", value = "所属用户ID")
    private Integer userId;

    @ApiModelProperty(name = "gameId", value = "所属用户游戏ID")
    private Integer gameId;

    @ApiModelProperty(name = "agentId", value = "业主ID")
    private Integer agentId;

    @ApiModelProperty(name = "number", value = "银行卡卡位  1：1号卡位 2：2号卡位 3：3号卡位")
    private Integer number;

    @ApiModelProperty(name = "oldCardNo", value = "原银行卡号")
    private String oldCardNo;

    @ApiModelProperty(name = "oldCardName", value = "原银行卡归属银行名称")
    private String oldCardName;

    @ApiModelProperty(name = "oldCardAddress", value = "原银行卡开户行")
    private String oldCardAddress;

    @ApiModelProperty(name = "oldCardOwner", value = "原银行卡户主")
    private String oldCardOwner;

    @ApiModelProperty(name = "cardNo", value = "新银行卡号")
    private String cardNo;

    @ApiModelProperty(name = "cardName", value = "新银行卡归属银行名称")
    private String cardName;

    @ApiModelProperty(name = "cardAddress", value = "新银行卡开户行")
    private String cardAddress;

    @ApiModelProperty(name = "cardOwner", value = "新银行卡户主")
    private String cardOwner;

    @ApiModelProperty(name = "status", value = "状态 1 绑定成功  2 更换审核中  3 更换成功 4 拒绝申请 5 取消审核 6 解除绑定")
    private Integer status;

    @ApiModelProperty(name = "applyTime", value = "申请时间")
    private Date applyTime;

    @ApiModelProperty(name = "operator", value = "操作人")
    private String operator;

    @ApiModelProperty(name = "operateTime", value = "操作时间")
    private Date operateTime;

    @ApiModelProperty(name = "remark", value = "备注")
    private String remark;

    @ApiModelProperty(name = "operate", value = "操作类型 绑定/更换")
    private String operate;
}
