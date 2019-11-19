package com.lzkj.mobile.vo;

import java.util.List;

import lombok.Data;

@Data
public class PlatformVO {
	private String kindCode;
	private String kindName;
	private Integer gameKindId;
	private Integer maintain;
	private Integer gameStatus;
	private Integer sortId;
	private Integer gameTypeItem;
	private Integer kindMark;
	private List<AgentMobileKindConfigVO> list;
}
