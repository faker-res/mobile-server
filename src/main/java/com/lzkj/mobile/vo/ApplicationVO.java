package com.lzkj.mobile.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ApplicationVO {

    private Integer effectiveUser;   //有效用户数
    private Integer userDefinition;  //用户定义,1为存款额度,2为打码量
    private BigDecimal userDefinitionValue;    //有效用户定义值
    private Integer effectiveUserNullity;            //有效用户数达成状态,1为未满足,2为满足
    private Integer userDefinitionValueNullity;    //有效用户定义值达成状态,1为未满足,2为满足
    private Integer nullity = 0;

}
