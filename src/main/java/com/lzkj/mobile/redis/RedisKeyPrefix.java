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

}
