package com.lzkj.mobile.vo;

import lombok.Data;

import java.util.List;

@Data
public class AwardOrderPageVo {
    private List<AwardOrderVo> applyOrderVo;

    private int pageCount;

    private int recordCount;
}
