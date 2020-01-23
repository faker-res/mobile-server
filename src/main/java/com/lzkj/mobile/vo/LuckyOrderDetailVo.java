package com.lzkj.mobile.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 幸运注单明细
 *
 * @author xxx
 */
@Data
public class LuckyOrderDetailVo {

    /**
     * 牌局编号
     */
    private String gameCode;

    /**
     * 中奖号码
     */
    private String luckyOrderNumber;

    /**
     * 手动派奖申请时间
     */
    private Date applyDate;

    /**
     * 手动派奖领取时间
     */
    private Date updateTime;

    /**
     * 自动派奖领取和申请时间
     */
    private Date createTime;

    /**
     * 开奖结果
     */
    private BigDecimal prizeAmount;


}
