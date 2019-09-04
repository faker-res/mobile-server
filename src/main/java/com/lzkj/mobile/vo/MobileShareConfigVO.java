package com.lzkj.mobile.vo;

import lombok.Data;

@Data
public class MobileShareConfigVO {
	private boolean valid;
	
	private int sharePresent;
	
	private int freeCount;
	
	private MobileDayTask dayTask;
		
	private int regGold;
	
	private String shareUrl;
	
	private String shareTitle;
	
	private String shareContent;
	
	private String earnShareContent;
	
	
}
