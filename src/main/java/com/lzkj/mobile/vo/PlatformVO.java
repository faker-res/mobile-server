package com.lzkj.mobile.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class PlatformVO implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String kindCode;
	private String kindName;
	private Integer gameKindId;
	private Integer maintain;
	private Integer gameStatus;
	private Integer sortId;
	private Integer gameTypeItem;
	private Integer kindMark;
	private List<AgentMobileKindConfigVO> list;
	private Integer bidPackageStatus;
}
