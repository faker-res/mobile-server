package com.lzkj.mobile.esdao;

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
@Document(indexName = "rukuan",type = "base")
public class OnLineOrderEs {

    @Id
    @Field(type=FieldType.Integer)
    private Integer onLineID; // 订单标识

    @Field(type=FieldType.Integer)
    private Integer operUserID; // 操作用户

    @Field(type=FieldType.Integer)
    private Integer shareID; // 服务标识

    @Field(type=FieldType.Integer)
    private Integer userID; // 用户标识

    @Field(type=FieldType.Integer)
    private Integer gameID; // 游戏ID

    @Field(type=FieldType.Text)
    private String accounts; // 用户名

    @Field(type=FieldType.Text)
    private String orderID; // 订单号码

    @Field(type=FieldType.Double)
    private BigDecimal orderAmount; // 订单金额

    @Field(type=FieldType.Double)
    private BigDecimal discountScale; // 折扣比例

    @Field(type=FieldType.Double)
    private BigDecimal payAmount; // 实付金额

    @Field(type=FieldType.Integer)
    private Integer rate;

    @Field(type=FieldType.Double)
    private BigDecimal currency; // 游戏豆数

    @Field(type=FieldType.Integer)
    private Integer orderStatus; // 订单状态  0:未付款;1:已付款待处理;2:处理完成'

    @Field(type=FieldType.Text)
    private String iPAddress; // 订单地址

    @Field(type = FieldType.Date,format = DateFormat.custom, pattern ="yyyy-MM-dd HH:mm:ss.SSS")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern ="yyyy-MM-dd HH:mm:ss.SSS", timezone = "GMT+8")
    private Date applyDate; // 订单日期

    @Field(type=FieldType.Integer)
    private Integer curType;

    @Field(type=FieldType.Integer)
    private Integer orderType;

    @Field(type=FieldType.Integer)
    private Integer payInfoID;

    @Field(type=FieldType.Text)
    private String payType;

    @Field(type=FieldType.Text)
    private String merchantOrderId;

    @Field(type=FieldType.Text)
    private String flowOrderID;

    @Field(type=FieldType.Text)
    private String operatorNameID;

    @Field(type=FieldType.Text)
    private String passagewayResponse;




}
