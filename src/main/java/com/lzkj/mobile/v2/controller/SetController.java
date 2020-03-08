package com.lzkj.mobile.v2.controller;

import com.lzkj.mobile.client.AccountsServiceClient;
import com.lzkj.mobile.config.SystemConstants;
import com.lzkj.mobile.exception.GlobeException;
import com.lzkj.mobile.redis.RedisDao;
import com.lzkj.mobile.redis.RedisKeyPrefix;
import com.lzkj.mobile.util.MD5Utils;
import com.lzkj.mobile.v2.common.Response;
import com.lzkj.mobile.v2.service.MailService;
import com.lzkj.mobile.vo.GlobeResponse;
import com.lzkj.mobile.vo.VerificationCodeVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Map;

/**
 *    
 *  *  
 *  * @Project: mobile-server 
 *  * @Package: com.lzkj.mobile.v2.controller 
 *  * @Description: TODO   
 *  * @Author:      
 *  * @CreateDate:  2020/2/16 18:51  
 *  * @Version:   v1.0
 *  *    
 *  
 */
@Slf4j
@RestController
@Api(tags = "设置按钮内部接口")
public class SetController {

    @Resource
    private RedisDao redisDao;
    @Resource
    private MailService mailService;
    @Resource
    private AccountsServiceClient accountsServiceClient;

    @GetMapping("/mobileInterface/resetInsurePwd")
    @ApiOperation(value = "修改余额宝密码", notes = "修改余额宝密码")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "s", value = "签名", paramType = "query", dataType = "String"),
    })
    public Response resetInsurePwd(Integer userId, String phone, String oldPwd, String newPwd, String verifyCode) {
        /*String key = RedisKeyPrefix.getKey(phone + ":BindPhone");
        VerificationCodeVO verificationCode = redisDao.get(key, VerificationCodeVO.class);
        if (verificationCode == null) {
            throw new GlobeException(SystemConstants.FAIL_CODE, "验证码无效，请重新获取验证码");
        }
        if (!verificationCode.getCode().equals(verifyCode)) {
            throw new GlobeException(SystemConstants.FAIL_CODE, "验证码错误");
        }
        if (System.currentTimeMillis() - verificationCode.getTimestamp() > 600000) {
            throw new GlobeException(SystemConstants.FAIL_CODE, "验证码已过期，请重新获取验证码");
        }
        if (oldPwd.equals(newPwd)) {
            throw new GlobeException(SystemConstants.FAIL_CODE, "新密码与旧密码一致");
        }*/
        String n = MD5Utils.MD5Encode(newPwd, "UTF-8").toUpperCase();
        String o = MD5Utils.MD5Encode(oldPwd, "UTF-8").toUpperCase();
        Response<Map<String, Object>> response = this.accountsServiceClient.resetInsurePwd(userId, o, n);
        mailService.send(userId, response);
        //redisDao.delete(key);
        return response;
    }

}
