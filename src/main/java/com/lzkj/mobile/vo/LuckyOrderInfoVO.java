package com.lzkj.mobile.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 幸运注单
 */
@Data
public class LuckyOrderInfoVO {
    /** 审核状态（1待申请、2审核中、3审核通过、4审核未通过）**/
    public static Integer APPLY_STATE_WAITING = 1;
    public static Integer APPLY_STATE_DEALING = 2;
    public static Integer APPLY_STATE_SUCCESS = 3;
    public static Integer APPLY_STATE_FAILED = 4;
    /** 派奖状态（1未派奖、2派奖中、3已派奖）**/
    public static Integer PRIZE_STATE_WAITING = 1;
    public static Integer PRIZE_STATE_DEALING = 2;
    public static Integer PRIZE_STATE_DONE = 3;

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
    /**
     * 游戏分类
     */
    private Integer kindType;
    /**
     * 游戏id
     */
    private Integer kindId	;

    /**
     * 游戏名称
     */
    private String gameName;

    /**
     * 奖励类型：1固定金额，2投注金额倍数，3打码金额倍数
     */
    private Integer awardType	;

    /**
     * 有效投注额
     */
    private BigDecimal betAmount;

    /**
     * 实际投注额
     */
    private BigDecimal realBetAmount;

    /**
     * 实际打码量
     */
    private BigDecimal realBuyNum;

    /**
     * 幸运注单号码
     */
    private String luckyOrderNumber;

    /**
     * 玩家游戏ID
     */
    private Integer gameId;

    /**
     * 奖金（当AwardType为固定金额时适用）(来自配置)
     */
    private Integer awardAmount;
    /**
     * 倍数（当AwardType为倍数时适用）(来自配置)
     */
    private Integer awardMultiple;
    /**
     * 号码匹配方式：1末尾连续，2随机连续(来自配置)
     */
    private Integer numberMatchType;
    /**
     * 参与次数限制(来自配置)
     */
    private Integer joinTimeLimit;

    /**
     * 开奖结果（对应RecordDrawScore.Score）
     */
    private BigDecimal resultScore;

    /**
     * 游戏时间（开始）
     */
    private Date gameStartTime;
}
