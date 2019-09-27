package com.lzkj.mobile.vo;

import lombok.Data;

@Data
public class MailVO {
    private Integer id;
    private String subject;
    private String content;
    private String insertDateTime;
    private Boolean isAutoCreated;
    private Boolean isOpen;
    private String sendDate;
}
