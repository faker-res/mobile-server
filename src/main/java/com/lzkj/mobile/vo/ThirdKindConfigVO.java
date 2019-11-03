package com.lzkj.mobile.vo;

import lombok.Data;

@Data
public class ThirdKindConfigVO {
	private Integer id;
	private String kindCode;
    private String kindName;
    private Integer nullity;
    private Integer agentId;
    private Integer sortId;
    private short gameStatus;//游戏状态   第三方为 0 不显示 1 火 2 维护中 默认为不显示
    private short isPlatform;//是否是平台
    private int gameKindId;
    private int typeId;
}
