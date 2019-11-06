package com.lzkj.mobile.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class QmDirectQueryVO {
   private Integer gameId;
    private Integer userId;
   private String nickName;
   private BigDecimal todayBet;
   private BigDecimal totalBet;
   private Integer teamMember;
   private Integer directMember;
}
