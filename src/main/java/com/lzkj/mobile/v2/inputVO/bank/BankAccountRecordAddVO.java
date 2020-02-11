package com.lzkj.mobile.v2.inputVO.bank;

import com.lzkj.mobile.v2.inputVO.BaseVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 *    
 *  *  
 *  * @Project: data-server 
 *  * @Package: com.lzkj.fund.model.inputVO 
 *  * @Description: TODO   
 *  * @Author:   horus   
 *  * @CreateDate:  2020/2/5 13:54  
 *  * @Version:   v1.0
 *  *    
 *  
 */
@Data
@ApiModel(value = "BankAccountRecordAddVO", description = "用户银行卡新增封装类")
public class BankAccountRecordAddVO  extends BaseVO {

    @ApiModelProperty(name = "gameId", value = "所属用户游戏ID", required = true)
    @NotNull(message = "gameId不能为空")
    private Integer gameId;

    @ApiModelProperty(name = "number", value = "银行卡卡位  1：1号卡位 2：2号卡位 3：3号卡位", allowableValues = "1,2,3", required = true)
    @NotNull(message = "number不能为空")
    private Integer number;

    @ApiModelProperty(name = "cardNo", value = "新银行卡号", required = true)
    @NotNull(message = "cardNo不能为空")
    private String cardNo;

    @ApiModelProperty(name = "cardName", value = "新银行卡归属银行名称", required = true)
    @NotNull(message = "cardName不能为空")
    private String cardName;

    @ApiModelProperty(name = "cardAddress", value = "新银行卡开户行", required = true)
    @NotNull(message = "cardAddress不能为空")
    private String cardAddress;

    @ApiModelProperty(name = "cardOwner", value = "新银行卡户主", required = true)
    @NotNull(message = "cardOwner不能为空")
    private String cardOwner;


}
