package com.lzkj.mobile.config;

/**
 * 系统设置的数据字典名称
 */
public enum AgentSystemEnum {
    BindMobileSend("BindMobileSend","绑定手机赠送"),
    VerificationIsOpen("VerificationIsOpen","开启游戏验证码"),
    ShowRealName("ShowRealName","注册填写真实姓名"),
    VIPOpen("VIPOpen","vip功能是否开启"),
    MailOpen("MailOpen","邮件系统是否开启"),
    SignOpen("SingUp","签到开关"),
    AgentRank("AgentRank","代理排行榜"),
    ResetPwd("ResetPwd","修改兑换密码"),
	ActivityOpen("ActivityOpen","活动展示"),
	ApplyOrderOpen("ApplyOrderOpen","提现展示"),
	YebOpen("YebOpen","余额宝是否开启"),
    WXDLOpen("wxdlopen","微信登录开关"),
    SJZCOpen("sjzcopen","手机登录开关"),
    BANKOPEN("BankOpen","注册时填写银行卡号"),
    REGISTEREDPHONEOPEN("RegisteredPhoneOpen","注册时手机号开关"),
    TXYEBPASSWORDOPEN("TXYEBpasswordOpen","提现时输入余额宝密码开关"),
    REGISTERACCOUNTOPEN("RegisterAccountOpen","注册帐号开关");

    private String name;

    private String describe;

    AgentSystemEnum(String name,String describe){
        this.name =name;
        this.describe =describe;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }
    public static  String getDescribe(String name){
        String describe =new String();
        for(AgentSystemEnum s: AgentSystemEnum.values()){
            if(name.equals(s.getName())){
                describe = s.getDescribe();
                break;
            }
        }
        return describe;
    }
}
