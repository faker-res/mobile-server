package com.lzkj.mobile.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class QmDirectQueryVO {
   private Integer gameId; //会员账号
    private Integer userId;
   private String nickName;//昵称
   private BigDecimal todayBet;//今日流水
   private BigDecimal totalBet;//总流水
   private Integer teamMember;//团队人数
   private Integer directMember;//直属人数
}
