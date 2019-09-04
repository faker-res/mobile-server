package com.lzkj.mobile.controller;

import com.lzkj.mobile.client.AccountsServiceClient;
import com.lzkj.mobile.config.SystemConstants;
import com.lzkj.mobile.exception.GlobeException;
import com.lzkj.mobile.redis.RedisDao;
import com.lzkj.mobile.redis.RedisKeyPrefix;
import com.lzkj.mobile.util.MD5Utils;
import com.lzkj.mobile.vo.GlobeResponse;
import com.lzkj.mobile.vo.VerificationCodeVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class RetrieveAccount {

    @Autowired
    private RedisDao redisDao;

    @Autowired
    private AccountsServiceClient accountsServiceClient;

    @RequestMapping("/updatepwd")
    private GlobeResponse<Object> updatePwd(String phone, String telCode, String logonPass, Integer agentId) {
        if(  agentId ==null || agentId == 0){
            throw new GlobeException(SystemConstants.FAIL_CODE, "参数错误!");
        }
        if(logonPass == null || logonPass.length() < 6) {
            throw new GlobeException(SystemConstants.FAIL_CODE, "密码长度不能低于6位");
        }
        if(phone == null || phone.length() < 11) {
            throw new GlobeException(SystemConstants.FAIL_CODE, "手机号格式错误");
        }
        String key = RedisKeyPrefix.getKey(phone + ":SetPass");
        VerificationCodeVO verificationCode = redisDao.get(key, VerificationCodeVO.class);
        if(verificationCode == null) {
            throw new GlobeException(SystemConstants.FAIL_CODE, "验证码无效，请重新获取验证码");
        }
        if(!verificationCode.getCode().equals(telCode)) {
            throw new GlobeException(SystemConstants.FAIL_CODE, "验证码错误");
        }
        if(System.currentTimeMillis() - verificationCode.getTimestamp() > 600000) {
            throw new GlobeException(SystemConstants.FAIL_CODE, "验证码已过期，请重新获取验证码");
        }
        String password  = MD5Utils.MD5Encode(logonPass, "utf-8");
        Map responseData = accountsServiceClient.updatePassword(phone,password,agentId);
        GlobeResponse<Object> response =new GlobeResponse<>();
        response.setData(responseData);
        return response;
    }
}
