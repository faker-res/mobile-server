package com.lzkj.mobile.vo;

import lombok.Data;

import java.util.List;

@Data
public class QmLiquidationPageVo {
    private List<QmLiquidationRewardRecordVO> qmLiquidationRewardRecordVO;

    private int pageCount;

    private int recordCount;
}
