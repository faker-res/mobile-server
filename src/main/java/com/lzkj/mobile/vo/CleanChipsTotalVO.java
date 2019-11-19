package com.lzkj.mobile.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Data
public class CleanChipsTotalVO {
    private BigDecimal totalBet;//游戏打码量
    private BigDecimal totalValue;//洗码金额
    private Map<Integer, List<CleanChipsVO>> data;

}
