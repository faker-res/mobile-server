package com.lzkj.mobile.v2.enums;

/**
 *    
 *  *  
 *  * @Project: agent 
 *  * @Package: com.lzkj.agent.v2.model.enums 
 *  * @Description: TODO   manualDepositN层级设置后是有返利的，
 *                          Select top 10 * from RYTreasureDB.dbo.RecordPresentInfo
 *                          WHERE userId = 164711 ORDER BY CollectDate desc
 *
 *  * @Author:   horus   
 *  * @CreateDate:  2020/2/13 10:39  
 *  * @Version:   v1.0
 *  *    
 *  
 */
public enum SendTemplateCodeEnum {

    INSURE_PASS_CHANGE("insurePassChange", "安全密码修改"),
    ;

    private String code;
    private String name;

    SendTemplateCodeEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }



}
