package com.lzkj.mobile.service;

import com.lzkj.mobile.client.AccountsServiceClient;
import com.lzkj.mobile.config.SystemConstants;
import com.lzkj.mobile.exception.GlobeException;
import com.lzkj.mobile.vo.UserInfoVO;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CheckUserSignatureService {
    @Autowired
    private AccountsServiceClient accountsServiceClient;


    public void checkUserSignature(int userId) {
        //查询用户信息
        UserInfoVO userInfoVo = accountsServiceClient.getUserInfo(userId, 0, "");
        if (userInfoVo == null) {
            throw new GlobeException(SystemConstants.FAIL_CODE, "用户不存在");
        }
        if (StringUtils.isBlank(userInfoVo.getDynamicPass())) {
            throw new GlobeException(SystemConstants.FAIL_CODE, "用户数据错误");
        }
//        //验证签名
//        StringBuffer signatureBuff = new StringBuffer();
//        signatureBuff.append(userInfoVo.getUserId());
//        signatureBuff.append(userInfoVo.getDynamicPass());
//        signatureBuff.append(time);
//        //AppConfig.SyncLoginKey
//        signatureBuff.append("RYSyncLoginKey");
//        String mdSignature = MD5Utils.MD5Encode(signatureBuff.toString(), "utf-8");
//        if (!signature.equals(mdSignature)) {
//            throw new GlobeException(SystemConstants.FAILCODE, "签名错误");
//        }
    }
}
