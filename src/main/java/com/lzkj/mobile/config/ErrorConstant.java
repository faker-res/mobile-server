package com.lzkj.mobile.config;

public enum  ErrorConstant {

    //公共的
    COMMON_001("COMMON_001"),

    //用户模块的

    //Redis
    REDIS_001("REDIS_001","用户token存在重复的key");

    ErrorConstant(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    ErrorConstant(String code) {
        this.code = code;
    }

    private String code;
    private String msg;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
