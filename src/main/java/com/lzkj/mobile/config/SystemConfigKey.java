package com.lzkj.mobile.config;

public enum SystemConfigKey {

	RateCurrency("RateCurrency"), RateGold("RateGold"), MedalExchangeRate("MedalExchangeRate"),
	PresentExchangeRate("PresentExchangeRate"), MobilePlatformVersion("MobilePlatformVersion"), PayConfig("PayConfig"),
	SharePresent("SharePresent"), WxLogon("WxLogon"), OpenMobileWebsite("OpenMobileWebsite"),
	TransferStauts("TransferStauts"), IOSNotStorePaySwitch("IOSNotStorePaySwitch"),
	AgentRoomCardExchRate("AgentRoomCardExchRate"), IsOpenRoomCard("IsOpenRoomCard"), TransferRebate("TransferRebate");

	private String name;

	SystemConfigKey(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
}
