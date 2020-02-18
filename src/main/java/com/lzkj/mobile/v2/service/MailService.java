package com.lzkj.mobile.v2.service;

import com.lzkj.mobile.client.AccountsServiceClient;
import com.lzkj.mobile.v2.common.Response;
import com.lzkj.mobile.v2.dto.InternalMessageDto;
import com.lzkj.mobile.v2.enums.SendMailSourceEnum;
import com.lzkj.mobile.v2.enums.SendTemplateCodeEnum;
import com.lzkj.mobile.v2.inputVO.activity.ReceivingRedEnvelopeRainVO;
import com.lzkj.mobile.v2.inputVO.activity.ReceivingRedEnvelopeVO;
import com.lzkj.mobile.v2.util.telegram.TelegramUtil;
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
 *  * @Author:   horus   
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
     * 余额宝密码修改
     * @param userId
     */
    public void send(Integer userId, Response<Map<String, Object>> response) {
        try {
            if (!Response.SUCCESS.equals(response.getCode())) {
                return;
            }
            InternalMessageDto dto = new InternalMessageDto();
            dto.setCode(SendTemplateCodeEnum.INSURE_PASS_CHANGE.getCode());
            dto.setUserId(userId);
            dto.setType(SendMailSourceEnum.ONE.getCode());
            accountsServiceClient.sendMail(dto);
        } catch (Exception e) {
            e.printStackTrace();
            TelegramUtil.sendGroupDefault("余额宝密码修改发送邮件失败：" + e.getMessage());
        }
    }

    /**
     * 新版领取红包奖励
     * @param vo
     * @param response
     */
    public void send(ReceivingRedEnvelopeVO vo, Response<Map<String, Object>> response) {
        try {
            if (!Response.SUCCESS.equals(response.getCode())) {
                return;
            }
            Map<String, String> map = SendTemplateCodeEnum.getMapByKey(SendTemplateCodeEnum.RED);
            String code = map.get(String.valueOf(vo.getTypeId()));

            InternalMessageDto dto = new InternalMessageDto();
            dto.setCode(code);
            dto.setUserId(vo.getUserId());
            dto.setAward(vo.getScore());
            dto.setType(SendMailSourceEnum.TWO.getCode());
            accountsServiceClient.sendMail(dto);
        } catch (Exception e) {
            e.printStackTrace();
            TelegramUtil.sendGroupDefault("新版领取红包奖励发送邮件失败：" + e.getMessage());
        }
    }

    public void send(ReceivingRedEnvelopeRainVO vo, Response<Map<String, Object>> response) {
        try {
            if (!Response.SUCCESS.equals(response.getCode())) {
                return;
            }
            Map<String, Object> data = response.getData();
            String money = String.valueOf(data.get("money"));
            BigDecimal score = money == null ? new BigDecimal(0) : new BigDecimal(money);

            InternalMessageDto dto = new InternalMessageDto();
            dto.setCode(SendTemplateCodeEnum.RED_RAIN_REWARD.getCode());
            dto.setUserId(vo.getUserId());
            dto.setAward(score);
            dto.setType(SendMailSourceEnum.THREE.getCode());
            accountsServiceClient.sendMail(dto);
        } catch (Exception e) {
            e.printStackTrace();
            TelegramUtil.sendGroupDefault("领取红包雨红包发送邮件失败：" + e.getMessage());
        }
    }

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
            dto.setAward(amount);
            dto.setType(SendMailSourceEnum.FOUR.getCode());
            accountsServiceClient.sendMail(dto);
        } catch (Exception e) {
            e.printStackTrace();
            TelegramUtil.sendGroupDefault("第三方入款-支付回调发送邮件失败：" + e.getMessage());
        }
    }
}
