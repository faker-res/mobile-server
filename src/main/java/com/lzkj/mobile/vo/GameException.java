package com.lzkj.mobile.vo;

import lombok.Data;

@Data
public class GameException {
    private String errorMessage;
    private String file;
    private String line;
    private String message;
    private String error;

}
