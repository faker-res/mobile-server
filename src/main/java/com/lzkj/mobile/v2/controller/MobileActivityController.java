package com.lzkj.mobile.v2.controller;

import com.lzkj.mobile.client.AccountsServiceClient;
import com.lzkj.mobile.v2.common.Response;
import com.lzkj.mobile.v2.inputVO.activity.ReceivingRedEnvelopeVO;
import com.lzkj.mobile.v2.service.MailService;
import com.lzkj.mobile.v2.util.IPUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 *    
 *  *  
 *  * @Project: data-server 
 *  * @Package: com.lzkj.accounts.v2.controller.mobile 
 *  * @Description: TODO   
 *  * @Author:   horus   
 *  * @CreateDate:  2020/2/18 16:38  
 *  * @Version:   v1.0
 *  *    
 *  
 */
@Slf4j
@RestController
@Api(tags = "活动(红包)接口")
public class MobileActivityController {

    @Resource
    private AccountsServiceClient accountsServiceClient;
    @Resource
    private MailService mailService;


    @GetMapping("/mobileInterface/getReceivingRedEnvelopes")
    @ApiOperation(value = "新版领取红包奖励", notes = "登录红包/每日充值红包/累计充值红包/每日打码量红包/累计打码量红包")
    public Response getReceivingRedEnvelopes(ReceivingRedEnvelopeVO vo, HttpServletRequest request) {
        vo.setIp(IPUtils.getIp(request));
        Response<Map<String, Object>> response = accountsServiceClient.getReceivingRedEnvelope(vo);
        mailService.send(vo, response);
        return response;
    }

}
