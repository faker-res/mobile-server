package com.lzkj.mobile.vo;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class GlobeResponse<T> implements Serializable {

    /**
	 *
	 */
	private static final long serialVersionUID = 1L;
	/**
     * code 是请求返回状态，默认0是成功
     */
    private String code = "0";
    /**
     * msg 错误等描述
     */
    private String msg = "请求成功！";
    private T data;

    public void setGlobeResponse(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }


}
