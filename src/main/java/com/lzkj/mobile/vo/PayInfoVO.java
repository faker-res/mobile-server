package com.lzkj.mobile.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * 支付商信息表(第三方)
 */
@Data
public class PayInfoVO {
	private int id;
	/**支付商名称*/
	private String payTypeName;
	/**支付通道编码*/
	private String payTypeCode;
	/**支付通道编码名称*/
	private String payType ;

	/**设置好的金额列表*/
	private List<BigDecimal> limit;

	private String type = "0";
	/**渠道*/
	private int  qudaoId;
	/**渠道名称*/
	private String  qudaoName;
	private String  memberId;
	private String  memberKey;
	private String  sendUrl;
	private short payMode;

	private String gameRemark;

	private AgentRebateConfigVO configVO;
	private String remark;
	private Boolean customizePay;
	private Double rebate;
}
