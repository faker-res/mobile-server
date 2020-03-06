package com.lzkj.mobile.v2.enums;

import java.util.HashMap;
import java.util.Map;

/**
 *    
 *  *  
 *  * @Project: agent 
 *  * @Package: com.lzkj.agent.v2.model.enums 
 *  * @Description: TODO   临时对应关系保持以前业务
 *  * @Author:      
 *  * @CreateDate:  2020/2/13 10:39  
 *  * @Version:   v1.0
 *  *    
 *  
 */
public enum SendMailSourceEnum {

    ONE(11, "余额宝密码修改"),
    TWO(12, "新版领取红包奖励"),
    THREE(13, "红包雨奖励"),
    FOUR(14, "第三方支付-支付回调"),
    ;

    private int code;
    private String desc;

    SendMailSourceEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public static Map<Integer,String> toMap(){
        Map<Integer,String> map = new HashMap<>();
        for (SendMailSourceEnum temp : SendMailSourceEnum.values()) {
            map.put(temp.getCode(), temp.getDesc());
        }
        return map;
    }

}
