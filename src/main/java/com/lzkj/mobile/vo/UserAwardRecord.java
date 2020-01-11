package com.lzkj.mobile.vo;

import lombok.Data;

import java.util.Date;

/**
 * 用户领奖记录（用户记录领奖级别和时间）
 */
@Data
public class UserAwardRecord {
    private Integer id;
    private Integer agentId;
    private Integer userId;
    /**
     * 领取序号（天数、级别，等）
     */
    private Integer awardIndex;
    private Date lastAcceptTime;
    private Integer awardId;
    /**
     * 奖励类型：1签到
     */
    private Integer awardType;
}
