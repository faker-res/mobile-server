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
@ApiModel(value = "BankAccountVO", description = "用户银行卡信息数据封装类")
public class BankAccountVO {

    @ApiModelProperty(value = "主键ID")
    private Integer id;

    @ApiModelProperty(value = "所属用户游戏ID")
    private Integer gameId;

    @ApiModelProperty(value = "银行卡卡位  1：1号卡位 2：2号卡位 3：3号卡位")
    private Integer number;

    @ApiModelProperty(value = "银行卡所属银行编码")
    private String cardCode;

    @ApiModelProperty(value = "银行卡号")
    private String cardNo;

    @ApiModelProperty(value = "银行卡归属银行名称")
    private String cardName;

    @ApiModelProperty(value = "银行卡开户行")
    private String cardAddress;

    @ApiModelProperty(value = "银行卡户主")
    private String cardOwner;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "是否为入款卡 0：否 1：是")
    private Integer isUse;

    @ApiModelProperty(value = "银行卡审核流水表ID")
    private Integer recordId;

    @ApiModelProperty(value = "状态 1 绑定成功  2 更换审核中  3 更换成功 4 拒绝申请 5 取消审核 6 解除绑定")
    private Integer status;

}
