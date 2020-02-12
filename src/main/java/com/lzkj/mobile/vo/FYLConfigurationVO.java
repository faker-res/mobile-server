package com.lzkj.mobile.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class FYLConfigurationVO {


    private BigDecimal profit;     //盈利总额
    private Integer effectiveUser;    //盈利总额的有效人数
    private BigDecimal commissionLimit;     //可领取佣金上限
    private String configurations;     //配置参数,JSON字符串格式

    private Integer backWaterValue;    //返水比例
    private Integer discountValue;     //优惠比例
    private Integer otherFeeValue;     //其他比例

    private Integer qpplValue;    //棋牌批量比例
    private Integer kyqpValue;    //开元棋牌比例
    private Integer xsjqpValue;    //新世界棋牌比例
    private Integer ttqpValue;    //天天棋牌比例
    private Integer jdbqpValue;    //JDB棋牌比例
    private Integer cq9qpValue;    //CQ9棋牌比例
    private Integer fgqpValue;    //FG棋牌比例

    private Integer byplValue;    //棋牌批量比例
    private Integer ttbyValue;    //天天捕鱼比例
    private Integer jdbbYValue;    //JDB捕鱼比例
    private Integer bgbyValue;    //BG捕鱼比例
    private Integer cq9bYValue;    //CQ9捕鱼比例
    private Integer agbyValue;    //AG捕鱼比例
    private Integer fgbyValue;    //FG捕鱼比例

    private Integer dzplValue;    //电子批量比例
    private Integer ttdzValue;    //JDB电子比例
    private Integer jdbdzValue;    //JDB电子比例
    private Integer mgdzValue;    //MG电子比例
    private Integer cq9dzValue;    //CQ9电子比例
    private Integer agdzValue;    //AG电子比例
    private Integer fgdzValue;    //FG电子比例
    private Integer twdzValue;    //TW电子比例

    private Integer zrplValue;    //真人批量比例
    private Integer agsxValue;    //AG视讯比例
    private Integer bgsxValue;    //BG视讯比例
    private Integer dgsxValue;    //DG视讯比例

}
