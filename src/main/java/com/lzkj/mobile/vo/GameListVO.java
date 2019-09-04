package com.lzkj.mobile.vo;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class GameListVO {
	private boolean valid;
	
	private String downloadUrl;
	
	private String debugUrl;
	
	private int wxLogon;
	
	private int isOpenCard;
	
	private List<MobileKind> gameList;
	
	private String packageName;
	
	private String iosUrl;
	
	private String clientVersion;
	
	private String resVersion;
	
	private int appStore;
	
	private String iosVersion;
}
