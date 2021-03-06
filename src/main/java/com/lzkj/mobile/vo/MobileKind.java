package com.lzkj.mobile.vo;

import lombok.Data;

import java.util.List;

@Data
public class MobileKind {
	private Integer kindId;
	private String kindName;
	private int sortId;
	private int gameStatus;
	private List<Integer> typeId;
	private List<RoomOpenVO> roomList;

}
