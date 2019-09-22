package com.lzkj.mobile.config;

/**
 * 系统设置的数据字典名称
 */
public enum AgentSystemEnum {
    BindMobileSend("BindMobileSend","绑定手机赠送"),
    VerificationIsOpen("VerificationIsOpen","开启游戏验证码"),
    ShowRealName("ShowRealName","注册填写真实姓名"),
    EnjoinLogon("EnjoinLogon","系统维护"),
    VIPOpen("VIPOpen","vip功能是否开启"),
    MailOpen("MailOpen","邮件系统是否开启"),
    SignOpen("SingUp","签到开关"),
    AgentRank("AgentRank","代理排行榜");
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
