package com.lzkj.mobile.vo;

import lombok.Data;

@Data
public class NewsVO {

    private int newsId;

    private int popId;

    private String subject;

    private String subject1;

    private byte onTop;

    private byte onTopAll;

    private byte isElite;

    private byte isHot;

    private byte isLock;

    private byte isDelete;

    private byte isLinks;

    private String linkUrl;

    private String body;

    private String formattedBody;

    private String highLight;

    private byte classId;

    private String gameRange;

    private String imageUrl;

    private int userId;

    private String issueIp;

    private String lastModifyIp;

    private String IssueDate;

    private String LastModifyDate;
    
    private int id;
}
