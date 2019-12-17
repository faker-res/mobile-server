package com.lzkj.mobile.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 幸运注单配置
 */
@Data
public class LuckyOrderConfigVO {
    private Integer configId;
    /**
     * 业主ID
     */
    private Integer agentId	;
    /**
     * 可用状态：0关闭，1启用（默认关闭）
     */
    private Integer enableState;
    /**
     * 游戏分类
     */
    private Integer kindType;
    /**
     * 游戏id
     */
    private Integer kindId	;
    /**
     * 号码匹配方式：1末尾连续，2随机连续
     */
    private Integer numberMatchType	;
    /**
     * 参与次数限制
     */
    private Integer joinTimeLimit	;
    /**
     * 派奖方式：1自动，2人工
     */
    private Integer prizeType	;
    /**
     * 有效投注额
     */
    private BigDecimal betAmount;
    private Date createTime;
    private Date updateTime;

    private List<LuckyOrderConfigItemVO> itemList;
}
