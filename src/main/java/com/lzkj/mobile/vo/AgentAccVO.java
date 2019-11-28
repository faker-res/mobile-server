package com.lzkj.mobile.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class AgentAccVO {
    private Integer agentId ;
    private String agentAcc = "";    //代理账号
    private String pwd = "";         //代理密码
    private Integer parentId = 0;
    private String parentAcc = "";
    private String agentLevel ="";
    private String safePwd ="";
    private short agentStatus = 0;
    private BigDecimal score= BigDecimal.ZERO;
    private BigDecimal agentRate = BigDecimal.ZERO;     //代理抽水
    private String qq = "";
    private String realName="";    //真实姓名
    private String agentDomain="";  //代理域名
    private String memo = "";          //备注
    private String bankAcc = "";
    private String bankName = "";
    private String bankAddress="";
    private String regIp = "";
    private String regDate = "";
    private String lastIp = "";
    private String lastDate = "";
    private Integer nagentNum=0;
    private Integer playerCount=0;
    private String weChat = "";           //微信
    private short isClient = 0;      //代理类别
    private String showName ="";   //显示名称
    private Integer showSort = 0;    //排序
    private Integer queryRight = 0;    //操作权限
    private String desKey = "";
    private String md5Key = "";
    private String clientIp; //操作IP
    private Integer operator; // 操作ID
    private String phoneName;
    private String phonePwd;
    private Integer qmMode;
    private Integer sendMode=0;
    private String agentUrl="";
    private Integer agentVersion;
    private String clientUrl;
    private String updateAddress;
    private String prompt="";   //维护提示
    private String PreUpdateAddress; //熱更地址
    private short status = 0;
    private String primaryDomain;
    private String hotVersion;

}
