package com.lzkj.mobile.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class RecordInsureVO {
   private int recordId;
   private int kindId;
   private int serverId;
   private int sourceUserId;
   private BigDecimal sourceGold;
   private int  targetUserId;
   private BigDecimal targetGold;
   private BigDecimal  targetBank;
   private BigDecimal swapScore;
   private BigDecimal revenue;
   private short isGamePlaza;
   private short tradeType;
   private String clientIp;
   private String collectDate;
   private String  collectNote;
   
   
}
