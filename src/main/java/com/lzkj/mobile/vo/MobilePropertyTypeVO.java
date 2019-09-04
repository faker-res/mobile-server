package com.lzkj.mobile.vo;

import java.util.List;

public class MobilePropertyTypeVO {
	private boolean valid;
	
	private List<GamePropertyType> list;

	public boolean isValid() {
		return valid;
	}

	public void setValid(boolean valid) {
		this.valid = valid;
	}

	public List<GamePropertyType> getList() {
		return list;
	}

	public void setList(List<GamePropertyType> list) {
		this.list = list;
	}
	
	
}
