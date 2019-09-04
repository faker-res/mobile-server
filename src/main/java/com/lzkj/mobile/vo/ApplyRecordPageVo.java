package com.lzkj.mobile.vo;

import lombok.Data;

import java.util.List;

@Data
public class ApplyRecordPageVo {
    private List<ApplyOrderVo> applyOrderVo;

    private int pageCount;

    private int recordCount;

}
