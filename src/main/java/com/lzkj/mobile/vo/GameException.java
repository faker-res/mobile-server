package com.lzkj.mobile.vo;

import lombok.Data;

@Data
public class GameException {
    private String time;
    private String yeZhu;
    private String error;
    private String errorMessage;
    private String file;
    private String line;
    private String message;
    private String httpSend;
    private String scene;
    private String socketMainCode;
    private String socketSubCode;
    private String eventName;
    private String newHandle;
    private String kindId;
    private String platform;

}
