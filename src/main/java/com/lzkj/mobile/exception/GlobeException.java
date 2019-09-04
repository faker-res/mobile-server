package com.lzkj.mobile.exception;

import com.lzkj.mobile.config.ErrorConstant;
import lombok.Data;

@Data
public class GlobeException extends RuntimeException{
    private String code;
    private String msg;

    public GlobeException(ErrorConstant e) {
        this.msg = e.getMsg();
        this.code = e.getCode();
    }

    public GlobeException(ErrorConstant e, String str) {
        this.msg = str;
        this.code = e.getCode();
    }

    public GlobeException(String failCode, String message) {
        this.msg = message;
        this.code = failCode;
    }
}
