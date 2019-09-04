package com.lzkj.mobile.vo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VerificationCodeVO {
	private long timestamp = System.currentTimeMillis();
	private String code;
}
