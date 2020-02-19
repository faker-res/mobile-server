package com.lzkj.mobile.vo;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class ActivityReviewRecordVO {
	private Integer recordId;
	private String title;
	private BigDecimal gold;
	private String applyEndTime;
	private Integer status;
	private String description;
}
