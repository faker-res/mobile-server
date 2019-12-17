package com.lzkj.mobile.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 幸运注单
 */
@Data
public class LuckyOrderInfoVO {

    private Integer id;
    /**
     * 业主ID
     */
    private Integer agentId	;
    /**
     * 对应RecordDrawScore.GameCode
     */
    private String gameCode;
    /**
     * 对应RecordDrawScore.UserID
     */
    private Integer userId	;
    /**
     * 对应LucyOrderConfig主键
     */
    private Integer configId;
    /**
     * 派奖方式：1自动，2人工
     */
    private Integer prizeType;
    /**
     * 派奖金额
     */
    private BigDecimal prizeAmount;
    /**
     * 派奖状态（1未派奖、2派奖中、3已派奖）
     */
    private Integer prizeState;
    /**
     * 审核状态（1待申请、2审核中、3审核通过、4审核未通过）
     */
    private Integer applyState;
    private String remark	;
    private Date createTime;
    private Date updateTime;
}
