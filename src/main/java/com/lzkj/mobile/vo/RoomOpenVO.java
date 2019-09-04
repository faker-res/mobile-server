package com.lzkj.mobile.vo;

import lombok.Data;

@Data
public class RoomOpenVO {
    private Integer serverId;
    private Integer kindId;
    private String serverName;
    private Boolean openType;
    private Integer serverLevel;
}
