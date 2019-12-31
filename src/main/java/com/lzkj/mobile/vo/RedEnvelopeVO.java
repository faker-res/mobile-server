package com.lzkj.mobile.vo;

import lombok.Data;

@Data
public class RedEnvelopeVO {
	private Integer id;
	private Integer eventId;
	private Integer limitedNumber;
	private Long dayStartTime;
	private Long dayEndTime;
	private Integer status;
	private Integer activityId;
}
