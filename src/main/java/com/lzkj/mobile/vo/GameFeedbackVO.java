package com.lzkj.mobile.vo;

import lombok.Data;


@Data
public class GameFeedbackVO {
    private Integer id;
    private Integer feedBackType;
    private String feedBackTxt;
    private String feedBackTime;
    private String replyTxt;
    private String replyTime;
}
