package com.lzkj.mobile.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.math.BigDecimal;
import java.util.Date;

@Data
@Document(indexName = "chukuan",type = "base")
public class ApplyOrderEs {

    @Id
    @Field(type= FieldType.Integer)
    private Integer iD;

    @Field(type=FieldType.Text)
    private String orderID; // 订单号

    @Field(type=FieldType.Integer)
    private Integer applicantID; // 提现玩家UserID

    @Field(type=FieldType.Double)
    private BigDecimal sellScore; // 提现扣除的Score数

    @Field(type=FieldType.Double)
    private BigDecimal sellMoney; // 提现所得RMB数

    @Field(type=FieldType.Double)
    private BigDecimal revenue; // 提现所扣手续费

    @Field(type = FieldType.Date,format = DateFormat.custom, pattern ="yyyy-MM-dd HH:mm:ss.SSS")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern ="yyyy-MM-dd HH:mm:ss.SSS", timezone = "GMT+8")
    private Date applyDate; // 玩家申请提现时间

    @Field(type=FieldType.Text)
    private String bankName; // 银行名

    @Field(type=FieldType.Text)
    private String bankDetail; // 开户行地址

    @Field(type=FieldType.Text)
    private String bankNum; // 银行卡号

    @Field(type=FieldType.Text)
    private String realName; // 开户人姓名

    @Field(type = FieldType.Date,format = DateFormat.custom, pattern ="yyyy-MM-dd HH:mm:ss.SSS")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern ="yyyy-MM-dd HH:mm:ss.SSS", timezone = "GMT+8")
    private Date operateDate; // 管理员处理提现申请时间

    @Field(type=FieldType.Integer)
    private Integer status; // 单状态 1申请提现 2管理员同意提现 3管理员拒绝提现 4等待付款状态

    @Field(type=FieldType.Text)
    private String rejectReason; // 拒绝提现的理由

    @Field(type=FieldType.Text)
    private String operator; // 审核账号

    @Field(type=FieldType.Integer)
    private Integer orderLock; // 提现锁定

    @Field(type=FieldType.Double)
    private BigDecimal actualAmountOfCoding; // 实际打码量

    @Field(type=FieldType.Double)
    private BigDecimal betAmountCount; // 需要打码量

    @Field(type=FieldType.Text)
    private String artificialRemark; //
}
