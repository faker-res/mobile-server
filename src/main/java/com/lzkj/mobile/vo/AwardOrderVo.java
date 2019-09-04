package com.lzkj.mobile.vo;

import lombok.Data;

@Data
public class AwardOrderVo {
    private int orderId;

    private int userId;

    private int awardId;

    private int awardPrice;

    private int awardCount;

    private int totalAmount;

    private String compellation;

    private String mobilePhone;

    private String QQ;

    private int province;

    private int city;

    private int area;

    private String dwellingPlace;

    private String postalCode;

    private int orderStatus;

    private String buyIP;

    private String buyDate;

    private String SolveNote;

    private String solveDate;

}
