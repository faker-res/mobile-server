package com.lzkj.mobile.vo;

import java.math.BigDecimal;

public class LotteryConfigVO {
	private int id;
	
	private int freeCount;
	
	private BigDecimal chargeFee;
	
	private byte IsCharge;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getFreeCount() {
		return freeCount;
	}

	public void setFreeCount(int freeCount) {
		this.freeCount = freeCount;
	}

	public BigDecimal getChargeFee() {
		return chargeFee;
	}

	public void setChargeFee(BigDecimal chargeFee) {
		this.chargeFee = chargeFee;
	}

	public byte getIsCharge() {
		return IsCharge;
	}

	public void setIsCharge(byte isCharge) {
		IsCharge = isCharge;
	}
	
	
}
