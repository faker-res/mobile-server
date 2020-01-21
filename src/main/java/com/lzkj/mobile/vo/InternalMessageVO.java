package com.lzkj.mobile.vo;

import lombok.Data;

@Data
public class InternalMessageVO {
	private int id;
	private String subject;
	private String content;
	private Integer userId;
	private Integer gameId;
	private Integer agentId;
	private Integer status;
	private String createTime;
	private String creater;
	private String updateTime;
	private String updater;
}
