package com.lzkj.mobile.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class AgentMobileKindConfigVO implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String kindName;
    private String kindCode;
    private Integer gameKindID;
    private Integer maintain;
    private Integer isPlatform;
    private Integer gameTypeItem;
    private Integer kindMark;
    private List<RoomOpenVO> roomList;
    private String imageUrl;
    private Integer subscript;
    private String iconImgUrl;
    private int resVersion;
    private int bidPackageStatus;
}
