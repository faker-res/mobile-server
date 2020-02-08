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
@ApiModel(value = "BankAccountVO", description = "用户银行卡信息数据封装类")
public class BankAccountVO {

    @ApiModelProperty(name = "id", value = "主键ID")
    private Integer id;

    @ApiModelProperty(name = "gameId", value = "所属用户游戏ID")
    private Integer gameId;

    @ApiModelProperty(name = "number", value = "银行卡卡位  1：1号卡位 2：2号卡位 3：3号卡位", allowableValues = "1,2,3")
    private Integer number;

    @ApiModelProperty(name = "cardNo", value = "银行卡号")
    private String cardNo;

    @ApiModelProperty(name = "cardName", value = "银行卡归属银行名称")
    private String cardName;

    @ApiModelProperty(name = "cardAddress", value = "银行卡开户行")
    private String cardAddress;

    @ApiModelProperty(name = "cardOwner", value = "银行卡户主")
    private String cardOwner;

    @ApiModelProperty(name = "createTime", value = "创建时间")
    private Date createTime;

    @ApiModelProperty(name = "isUse", value = "是否为入款卡 0：否 1：是")
    private Integer isUse;

    @ApiModelProperty(name = "recordId", value = "银行卡审核流水表ID")
    private Integer recordId;

}
