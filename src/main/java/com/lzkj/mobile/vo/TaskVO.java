package com.lzkj.mobile.vo;

import lombok.Data;

@Data
public class TaskVO {
	private String taskDescription;
	private String taskBeginTime;
	private String taskEndTime;
	private Integer taskPeriod;
	private short taskType;
	private Integer kindId;
	private Integer taskTypeValue;
	private String thirdItemValue;
	private Integer innings;
	private String taskRang;
	private String taskExplain;
}
