package com.lzkj.mobile.v2.util.telegram;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

/**
 *    
 *  *  
 *  * @Project: data-server 
 *  * @Package: com.lzkj.treasure.v2.util.telegram 
 *  * @Description: TODO   
 *  * @Author:   horus   
 *  * @CreateDate:  2020/2/10 17:33  
 *  * @Version:   v1.0
 *  *    
 *  
 */
@Slf4j
public class TelegramUtil {

    private static String profile;

    @Value("${spring.cloud.config.profile}")
    public void setProfile(String profile) {
        TelegramUtil.profile = profile;
    }

    /**
     * 默认监控机器人 horus专用
     * @param msg
     */
    public static void sendDefault(String msg) {
        if (msg == null) {
            return;
        }
        msg = profile + " " + msg;
        try {
            TelegramBotUtil.sendAlertMessage(TelegramConstant.ChatId.HORUS_BOT, TelegramConstant.Token.HORUS_TOKEN, msg);
        } catch (Exception e) {
            log.info("预警-发送失败{}", e);
        }
    }

    /**
     * 默认监控群组1
     * @param msg
     */
    public static void sendGroupDefault(String msg) {
        if (msg == null) {
            return;
        }
        msg = profile + " " + msg;
        try {
            TelegramBotUtil.sendAlertMessage(TelegramConstant.ChatId.HORUS_BOT_1, TelegramConstant.Token.HORUS_TOKEN, msg);
        } catch (Exception e) {
            log.info("预警-发送失败{}", e);
        }
    }

    public static void send(String msg, String chatId) {
        if (msg == null) {
            return;
        }
        msg = profile + " " + msg;
        try {
            TelegramBotUtil.sendAlertMessage(chatId, TelegramConstant.Token.HORUS_TOKEN, msg);
        } catch (Exception e) {
            log.info("预警-发送失败{}", e);
        }
    }

}
