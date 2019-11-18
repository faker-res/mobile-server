package com.lzkj.mobile.vo;

import java.util.List;

import lombok.Data;

@Data
public class AgentMobileKindConfigVO {
    private String kindName;
    private String kindCode;
    private Integer gameKindID;
    private Integer maintain;
    private Integer IsPlatform;
    private Integer gameTypeItem;
    private Integer kindMark;
    private List<RoomOpenVO> roomList;
    private String imageUrl;
}
