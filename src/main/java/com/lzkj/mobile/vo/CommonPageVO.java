package com.lzkj.mobile.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class CommonPageVO<E> {

    List<E> lists;

    private int pageCount;

    private int recordCount;

    private BigDecimal totalScort;

    //-玩家游戏抽水
    private BigDecimal sumAll;   // 游戏抽水-总税收
    private BigDecimal sumAgent;// 游戏抽水-代理总抽水
    private BigDecimal sum;// 游戏抽水-符合查询条件的税收总额

    private BigDecimal carryScore ;   //携带金币
    private BigDecimal strongboxScore;//保险柜
    private  BigDecimal revenueScore;//总税收

    private BigDecimal sumSellMoney;//提现总金额扣除手续费

}
