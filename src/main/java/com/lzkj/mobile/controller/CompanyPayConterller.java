package com.lzkj.mobile.controller;

import com.lzkj.mobile.client.TreasureServiceClient;
import com.lzkj.mobile.config.SystemConstants;
import com.lzkj.mobile.exception.GlobeException;
import com.lzkj.mobile.redis.RedisKeyPrefix;
import com.lzkj.mobile.redis.RedisLock;
import com.lzkj.mobile.vo.AgentCompanyRecordVO;
import com.lzkj.mobile.vo.AgentRebateConfigVO;
import com.lzkj.mobile.vo.CompanyPayVO;
import com.lzkj.mobile.vo.GlobeResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/companypay")
public class CompanyPayConterller {

    @Autowired
    private TreasureServiceClient treasureServiceClient;


    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    /**
     * 充值层级信息
     *
     * @return
     */
    @RequestMapping("/getCompanyPayType")
    private GlobeResponse<Object> getCompanyPayType(Integer agentId, String payType) {
        if (agentId == null || StringUtils.isBlank(payType)) {
            throw new GlobeException(SystemConstants.FAIL_CODE, "参数错误");
        }
        GlobeResponse<Object> globeResponse = new GlobeResponse<>();
        List<CompanyPayVO> list = treasureServiceClient.getCompanyPayType(agentId, payType);
        globeResponse.setData(list);
        return globeResponse;
    }

    /**
     * 公司充值-查询充值记录
     *
     * @param
     * @return
     */
    @RequestMapping("/getCompanyRecord")
    private GlobeResponse<Object> getCompanyRecord(Integer userId) {
        if (userId == null) {
            throw new GlobeException(SystemConstants.FAIL_CODE, "参数错误");
        }

        GlobeResponse<Object> globeResponse = new GlobeResponse<>();
        List<AgentCompanyRecordVO> list = treasureServiceClient.getRecordList(userId);
        globeResponse.setData(list);
        return globeResponse;
    }

    /**
     * 生成公司充值订单
     *
     * @param agentId
     * @param userId
     * @param gameId
     * @param payId  此处的payid 是指：通道id, 不是公司充值配置id
     * @param orderAmount
     * @param remarks
     * @return
     */
    @RequestMapping("/insertRecord")
    private GlobeResponse insertRecord(Integer agentId, Integer userId, Integer gameId, Integer payId, BigDecimal orderAmount,
                                       String remarks, String account) {
        if (agentId == null || userId == null || gameId == null || payId == null || orderAmount == BigDecimal.ZERO) {
            throw new GlobeException(SystemConstants.FAIL_CODE, "参数错误");
        }
        RedisLock redisLock = new RedisLock(RedisKeyPrefix.payLock(""+userId+orderAmount), redisTemplate, 10);
        Boolean hasLock = redisLock.tryLock();
        if (!hasLock) {
            log.error("公司支付没有下单成功,", userId);
            throw new GlobeException(SystemConstants.FAIL_CODE, "下单失败，请稍后重试");
        }
        try {
            Map map = new HashMap();
            String payName = "";
            Integer type = null;
            if (0 <= payId && payId <= 6) {
                switch (payId) {
                    case 0 : payName = "AliPay"; break;
                    case 1 : payName = "WeChatPay";break;
                    case 2 : payName = "BankPay";break;
                    case 3 : payName = "CloudPay";break;
                    case 4 : payName = "QQPay";break;
                    case 5 : payName = "JinDongPay";break;
                    case 6 : payName = "redPwd";break;
                }
                type =  treasureServiceClient.getPayId(agentId,payName);
                map = treasureServiceClient.insertRecord(agentId, userId, gameId,type, orderAmount, remarks, account);
            } else {
                //判断使用过程中通道是否被关闭了
                Integer isopen = treasureServiceClient.checkPayIdIsOpen(agentId,payId);
                if(1 == isopen){
                    throw new GlobeException(SystemConstants.FAIL_CODE, "支付通道已关闭,请选择其他通道,谢谢");
                }
                map = treasureServiceClient.insertRecord(agentId, userId, gameId, payId, orderAmount, remarks, account);
            }
            Integer ret = (Integer) map.get("ret");
            String strErrorDescribe = (String) map.get("strErrorDescribe");
            String mag = "";
            if (ret == 1) {
                mag = strErrorDescribe;
                throw new GlobeException(SystemConstants.FAIL_CODE, mag);
            }
            if (ret == 2) {
                mag = strErrorDescribe;
                throw new GlobeException(SystemConstants.FAIL_CODE, mag);
            }
            if (ret == 3) {
                mag = strErrorDescribe;
                throw new GlobeException(SystemConstants.FAIL_CODE, mag);
            }
            if (ret == 4) {
                mag = strErrorDescribe;
                throw new GlobeException(SystemConstants.FAIL_CODE, mag);
            }
        }finally {
            redisLock.unlock();
        }
        GlobeResponse globeResponse = new GlobeResponse();
        return globeResponse;
    }

    /**
     * 获取公司与银商充值返现数据
     *
     * @return
     */
    @RequestMapping("/getRebateInfo")
    private GlobeResponse getRebateInfo(Integer agentId, Integer userId) {
        if (agentId == null) {
            throw new GlobeException(SystemConstants.FAIL_CODE, "参数错误");
        }
        GlobeResponse<Object> globeResponse = new GlobeResponse<>();
        AgentRebateConfigVO agentRebateConfigVO = treasureServiceClient.getRebateInfo(agentId, userId);
        globeResponse.setData(agentRebateConfigVO);
        return globeResponse;
    }
}
