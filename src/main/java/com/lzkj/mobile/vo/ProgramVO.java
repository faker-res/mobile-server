package com.lzkj.mobile.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class ProgramVO {

    private Integer agentId;     //业主ID
    private String programName;        //方案名
    private String createTime;         //创建时间
    private Integer proxyNum;          //代理数
    private Integer giftMoneyConfiguration;    //赠送彩金配置,1为人工审核,2为后台自动添加
    private BigDecimal giftMoney;      //彩金额度
    private Integer depositVlaue;   //存款总额千分比
    private Integer codeValue;      //有效投注千分比
    private String configurations;      //配置
    private Integer isUpdate;     //是否为修改值,0为初始值,1为修改值
    private Integer isDefault;    //是否为默认方案,0为默认,1为业主新增
    private List<TTConfigurationVO> TTConfigurationList;     //天天返佣配置列表
    private Integer programType;     //方案类型,1为天天,2为负盈利

    private Integer id;   //负盈利方案ID
    private String sid;   //方案的唯一生成标识
    private Integer updateId;     //修改方案携带被修改方案的ID
    private List<FYLConfigurationVO> FYLConfigurationList;   //负盈利配置列表
    private Integer userDefinition;  //用户定义,1为存款额度,2为打码量
    private BigDecimal userDefinitionValue;  //有效用户定义值
    private String remarks; //备注
    private String newProgramID;
}
