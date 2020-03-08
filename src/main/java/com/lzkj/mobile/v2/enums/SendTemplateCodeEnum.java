package com.lzkj.mobile.v2.enums;

import java.util.HashMap;
import java.util.Map;

/**
 *    
 *  *  
 *  * @Project: agent 
 *  * @Package: com.lzkj.agent.v2.model.enums 
 *  * @Description: TODO   manualDepositN层级设置后是有返利的，
 *                          Select top 10 * from RYTreasureDB.dbo.RecordPresentInfo
 *                          WHERE userId = 164711 ORDER BY CollectDate desc
 *
 *  * @Author:      
 *  * @CreateDate:  2020/2/13 10:39  
 *  * @Version:   v1.0
 *  *    
 *  
 */
public enum SendTemplateCodeEnum {

    INSURE_PASS_CHANGE("insurePassChange", "安全密码修改", "", ""),
    RED_RAIN_REWARD("redRainReward", "红包雨", "", ""),
    THIRD_CHARGE_N("thirdChargeN", "第三方充值邮件-支付回调", "", ""),
    LOGON_RED_REWARD("logonRedReward", "登录红包", SendTemplateCodeEnum.RED, "1"),
    CHARGE_RED_REWARD("chargeRedReward", "每日充值红包", SendTemplateCodeEnum.RED, "2"),
    CHARGE_RED_REWARD_CUMULATIVE("chargeRedRewardCumulative", "累计充值红包", SendTemplateCodeEnum.RED, "3"),
    BET_RED_REWARD("betRedReward", "每日打码量红包", SendTemplateCodeEnum.RED, "4"),
    BET_RED_REWARD_CUMULATIVE("betRedRewardCumulative", "累计打码量红包", SendTemplateCodeEnum.RED, "5"),
    ;

    public static final String RED = "red";
    private String code;
    private String name;
    private String key;
    private String value;

    SendTemplateCodeEnum(String code, String name, String key, String value) {
        this.code = code;
        this.name = name;
        this.key = key;
        this.value = value;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    public static Map<String, String> getMapByKey(String key){
        Map<String, String> map = new HashMap<>();
        if(key != null){
            for (SendTemplateCodeEnum temp : SendTemplateCodeEnum.values()) {
                if(key.equals(temp.getKey())){
                    map.put(temp.getValue(), temp.getCode());
                }
            }
        }
        return map;
    }

}
