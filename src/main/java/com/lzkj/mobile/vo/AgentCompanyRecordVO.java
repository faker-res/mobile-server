package com.lzkj.mobile.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class AgentCompanyRecordVO {
//   private Integer  userId;   //用户Id
//   private Integer  gameId;   //游戏ID
//   private String  accounts; //用户账号
//   private String  payType;  //支付类型
//   private String  payTypeName;  //支付类型名称
//   private String  orderId;     //订单号
   private String shareName;   //支付类型
  // private BigDecimal orderAmount;  //订单金额
   private String  orderId;     //订单号
   private String  applyDate;    //订单时间
   private short  orderStatus;    //订单状态  0:未付款;1:已付款待处理;2:处理完成
   private BigDecimal  payAmount;    //实付金额
//   private Integer  agentId;
//   private String remarks;
}
