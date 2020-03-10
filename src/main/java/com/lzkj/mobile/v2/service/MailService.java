package com.lzkj.mobile.v2.service;

import com.lzkj.mobile.client.AccountsServiceClient;
import com.lzkj.mobile.v2.common.Response;
import com.lzkj.mobile.v2.dto.InternalMessageDto;
import com.lzkj.mobile.v2.enums.SendTemplateCodeEnum;
import com.lzkj.mobile.v2.inputVO.activity.ReceivingRedEnvelopeRainVO;
import com.lzkj.mobile.v2.inputVO.activity.ReceivingRedEnvelopeVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Map;

/**
 *    
 *  *  
 *  * @Project: mobile-server 
 *  * @Package: com.lzkj.mobile.v2.service 
 *  * @Description: TODO   职业邮件异步接口
 *  * @Author:      
 *  * @CreateDate:  2020/2/13 12:39  
 *  * @Version:   v1.0
 *  *    
 *  
 */
@Async
@Slf4j
@Service
public class MailService {

    @Resource
    private AccountsServiceClient accountsServiceClient;

    /**
     * 支付回调
     * @param userId
     * @param amount
     */
    public void send(Object userId, BigDecimal amount) {
        try {
            InternalMessageDto dto = new InternalMessageDto();
            dto.setCode(SendTemplateCodeEnum.THIRD_CHARGE_N.getCode());
            dto.setUserId(Integer.parseInt(userId.toString()));
            dto.setRecharge(amount);
            accountsServiceClient.sendMail(dto);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
