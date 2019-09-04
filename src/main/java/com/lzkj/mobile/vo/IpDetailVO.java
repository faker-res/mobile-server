package com.lzkj.mobile.vo;

//import org.springframework.data.mongodb.core.index.Indexed;
//import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
//@Document
public class IpDetailVO implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String ip;
	private String ip2;
	
//	@Indexed(background=true)
	private long ipNumber;
	
//	@Indexed(background=true)
	private long ipNumber2;
	
	private String remark1;
	private String remark2;
	
	public String toString() {
		return ip + "-" + ip2;
	}
}


