package com.lzkj.mobile.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class VipWelfareVO {
  private Integer userId;
  private Integer gameId;
  private Integer vipLevel;
  private BigDecimal vipIntegral;
  private List<VipRankReceiveVO> list ;
}
