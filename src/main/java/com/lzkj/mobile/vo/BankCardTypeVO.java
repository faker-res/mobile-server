package com.lzkj.mobile.vo;

import lombok.Data;

@Data
public class BankCardTypeVO {
	private Integer id;
	private Integer parentId;
	private String bankCardType;
	private Integer isOpen;
}
