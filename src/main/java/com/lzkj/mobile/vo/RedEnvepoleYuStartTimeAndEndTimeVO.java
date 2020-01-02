package com.lzkj.mobile.vo;


import lombok.Data;

@Data
public class RedEnvepoleYuStartTimeAndEndTimeVO {

	private Long dayStartTime;
	private Long dayEndTime;
	private Integer status;
	private Integer activityId;
	private Integer redAmount;
	private Long currentTime;
}
