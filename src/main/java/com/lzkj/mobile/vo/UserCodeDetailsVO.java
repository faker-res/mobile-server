package com.lzkj.mobile.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class UserCodeDetailsVO {
    private Integer userId;
    private Integer agentId;
    private Integer userLevel;
    private String applyDate;  //时间
    private BigDecimal inAmounts;  //需求打码量
    private BigDecimal codeAmountCount; //实际打码量
    private Integer status;  //状态

    //打码类型(0:表无打码，1、人工存入 2、线下充值 3、公司充值 4、公司充值返还 5、VIP/银商充值返还 6、人工充值返利
    //7、代理分红 8、VIP/银商充值  9、其他渠道充值(支付宝、微信、京东、云闪付、银联、快捷、比特币、苹果、百度、QQ等方式)
    //10、活动优惠  11、后台余额宝赠送 12、后台金币赠送 13、注册赠送 14、签到赠送 15、任务奖励  16、佣金提现 17、洗码
    // 18、每日登录红包 19、每日充值红包 20、累计充值红包 21、每日打码量红包 22、累计打码量红包 23、红包雨红包
    // 24、VIP赠送 25、每日首存赠送 26、后台修改 27、余额宝收益 28、幸运注单奖金)
    private Integer accountType;

    //未完成打码量
    private BigDecimal needAmount;
}
