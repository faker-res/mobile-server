package com.lzkj.mobile.interceptor;

import com.lzkj.mobile.config.SystemConstants;
import com.lzkj.mobile.exception.GlobeException;
import com.lzkj.mobile.redis.RedisDao;
import com.lzkj.mobile.redis.RedisKeyPrefix;
import com.lzkj.mobile.util.MD5Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
@Component
public class SignatureCheckInterceptor implements HandlerInterceptor {
	@Autowired
	private RedisDao redisDao;
	@Value("${spring.cloud.config.profile}")
	private String profile;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
		String path = request.getServletPath();
		String signatureKey;
		try {
			signatureKey = redisDao.get(RedisKeyPrefix.getSignatureKey(), String.class);
			if(signatureKey == null || signatureKey.equals("")) {
				signatureKey = "lz-123321.";
				redisDao.set(RedisKeyPrefix.getSignatureKey(), signatureKey);
			}
		}catch (Exception e){
			e.printStackTrace();
			log.info("请求异常:{}", e.getMessage());
			throw new GlobeException(SystemConstants.FAIL_CODE, "请求异常");
		}
		String signature = request.getParameter("s");
		log.info("SignatureCheckInterceptor获取spring.cloud.config.profile = {}", profile);
		if(!"prod".equals(profile) && "lzkj@123".equals(signature)){
			return true;
		}
		if(signature == null) {
			throw new GlobeException(SystemConstants.FAIL_CODE, "签名错误");
		}
		String signatureArgs = "";
		List<String> paramNames = new ArrayList<String>();
		for(String key : request.getParameterMap().keySet()) {
			paramNames.add(key);
		}
		if(paramNames.size() > 0) {
			Collections.sort(paramNames);
			for(String name : paramNames) {
				if(name.equals("s"))
					continue;
				signatureArgs += name + request.getParameter(name);
			}
		}
		signatureArgs = path + signatureArgs + signatureKey;
		String mdSignature = MD5Utils.MD5Encode(signatureArgs, "utf-8");
		if(!mdSignature.equals(signature.toLowerCase())) {
			throw new GlobeException(SystemConstants.FAIL_CODE, "签名错误");
		}
		return true;
	}

}
