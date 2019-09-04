package com.lzkj.mobile.vo;

import lombok.Data;

import java.util.List;

//@Getter
//@Setter
@Data
public class PayInfoVO {
	private int id;

	private String payTypeName;

	private String payTypeCode;

	private String payType ;

	private List<Integer> limit;

	private String type = "0";

	private int  qudaoId;
	private String  qudaoName;
	private String  memberId;
	private String  memberKey;
	private String  sendUrl;
	private short payMode;

	private String gameRemark;

	private AgentRebateConfigVO configVO;
	private String remark;
}
