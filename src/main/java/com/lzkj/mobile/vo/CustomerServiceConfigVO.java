package com.lzkj.mobile.vo;

import lombok.Data;

@Data
public class CustomerServiceConfigVO {
    private String name;            //客户名称
    private String timeSegment;     //工作时间段
    private Integer serviceType = 0;     //客户类型  全部 0 1 VIP客服  2 QQ客服  3微信客服
    private String serviceName;
    private String contactTheWay;   //联系方式   QQ号  微信号  客服地址
    private String imgUrl;          //图片
}
