package com.lzkj.mobile.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class QmPromotionDetailVO {
    private Integer userId;
    private String registerDate; //注册日期
    private Integer directMember;//直属人数
    private Integer teamMember;// 团队人数
    private BigDecimal commission;// 佣金
    private BigDecimal directBet;//直属业绩
    private BigDecimal teamBet;//团队业绩
}
