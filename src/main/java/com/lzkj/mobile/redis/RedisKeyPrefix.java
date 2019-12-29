package com.lzkj.mobile.redis;

public class RedisKeyPrefix {

	public static final String MOBILE_SERVER= "MOBILE_SERVER:";

	public static String getKey(String key) {
		return MOBILE_SERVER + key;
	}

	public static String getSignatureKey() {
		return MOBILE_SERVER + "signatureKey";
	}

	public static String getPayInfoKey(int qudaoId) {
		return MOBILE_SERVER + "payInfo:" + qudaoId;
	}

	public static String getTokenKey(String key) {
		return "ACCESS2GAME_SERVER:TOKEN:" + key;
	}

	public static String getAgentKey(Integer agentId){
		return  "ACCESS2GAME_SERVER:AGENT:" + agentId;
	}



	/**loginStatus相关**/
	public static String getQrCodeKey(Integer agentId) {
		return MOBILE_SERVER + "getQrCode:" + agentId;
	}

	public static String getSystemStatusInfoKey(String key) {
		return MOBILE_SERVER + "getSystemStatusInfoKey:" + key;
	}

	public static String  getAgentSystemStatusInfoKey(Integer agentId) {
		return MOBILE_SERVER + "getAgentSystemStatusInfo:" + agentId;
	}

	public static String getMobileKindList() {
		return MOBILE_SERVER + "getMobileKindList";
	}


	public static String getAgentGameListByGameTypeItemKey(Integer agentId) {
		return MOBILE_SERVER + "getAgentGameListByGameTypeItem:" + agentId;
	}

	public static String getAgentGameByGameTypeItemKey(Integer agentId) {
		return MOBILE_SERVER + "getAgentGameByGameTypeItem:" + agentId;
	}

	public static String getloginStatusCacheKey(Integer agentId, String registerMachine) {
		return MOBILE_SERVER + "getloginStatusCache:" + agentId + "_" + registerMachine;
	}

	public static String getControllerKey() {
		return MOBILE_SERVER + "getControllerKey:"  ;
	}

	public static String getCloudShieldConfigurationInfos(Integer agentId) {
		return MOBILE_SERVER + "getCloudShieldConfigurationInfos:" + agentId;
	}

	public static String getQrCode(Integer agentId) {
		return MOBILE_SERVER + "getgetQrCode:" + agentId;
	}

	public static String getLuckyIsOpen(Integer agentId) {
		return MOBILE_SERVER + "getLuckyIsOpen:" + agentId;
	}

	public static String getAgentGameListByGameTypeItemKeyStatus(Integer agentId) {
		return MOBILE_SERVER + "gettAgentGameListByGameTypeItemKeyStatus:" + agentId;
	}

	public static String getAgentGameByGameTypeItemKeyStatus(Integer agentId) {
		return MOBILE_SERVER + "getAgentGameByGameTypeItemKeyStatus:" + agentId;
	}

	public static String getGameListStatus(Integer agentId) {
		return MOBILE_SERVER + "getGameListStatus:" + agentId;
	}

	public static String getPayPageLoadSubmitLockKey(int userId) {
		return MOBILE_SERVER + "payPageLoadSubmitLockKey:" + userId;
	}

	/**
	 * 公司支付上锁
	 * @param key
	 * @return
	 */
	public static String payLock(String key){
		return MOBILE_SERVER+"payLock:"+key;
	}
	
	public static String shareJumpLinkKey() {
		return "MANAGER:PromoteDomainUrl";
	}
	
	public static String getShareParamKey(String param) {
		return "ShareParam:" + param;
	}
}
