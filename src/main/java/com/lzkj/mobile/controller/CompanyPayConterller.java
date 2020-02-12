package com.lzkj.mobile.controller;

import com.lzkj.mobile.client.TreasureServiceClient;
import com.lzkj.mobile.config.SystemConstants;
import com.lzkj.mobile.exception.GlobeException;
import com.lzkj.mobile.redis.RedisKeyPrefix;
import com.lzkj.mobile.redis.RedisLock;
import com.lzkj.mobile.vo.*;
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
    public GlobeResponse<Object> getCompanyPayType(Integer agentId, String payType) {
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
    public GlobeResponse<Object> getCompanyRecord(Integer userId) {
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
     * @return
     */
    @RequestMapping("/insertRecord")
    public GlobeResponse insertRecord(CompanyOnlineOrderVO vo) {
        if (vo.getAgentId() == null || vo.getUserId() == null || vo.getGameId() == null || vo.getPayId() == null
                || vo.getOrderAmount() .equals(BigDecimal.ZERO) ) {
            throw new GlobeException(SystemConstants.FAIL_CODE, "参数错误");
        }
        RedisLock redisLock = new RedisLock(RedisKeyPrefix.payLock(""+vo.getUserId()+vo.getOrderAmount()), redisTemplate, 10);
        Boolean hasLock = redisLock.tryLock();
        if (!hasLock) {
            log.error("公司支付没有下单成功,", vo.getUserId());
            throw new GlobeException(SystemConstants.FAIL_CODE, "下单失败，请稍后重试");
        }
        try {
            Map map = new HashMap();
            String payName = "";
            Integer type = null;
            if (0 <= vo.getPayId() && vo.getPayId() <= 6) {
                switch (vo.getPayId()) {
                    case 0 : payName = "AliPay"; break;
                    case 1 : payName = "WeChatPay";break;
                    case 2 : payName = "BankPay";break;
                    case 3 : payName = "CloudPay";break;
                    case 4 : payName = "QQPay";break;
                    case 5 : payName = "JinDongPay";break;
                    case 6 : payName = "redPwd";break;
                }
                type =  treasureServiceClient.getPayId(vo.getAgentId(),payName);
                map = treasureServiceClient.insertRecord(vo.getAgentId(), vo.getUserId(), vo.getGameId(),type,
                        vo.getOrderAmount(), vo.getRemarks(), vo.getAccount());
            } else {
                //判断使用过程中通道是否被关闭了
                Integer isopen = treasureServiceClient.checkPayIdIsOpen(vo.getAgentId(),vo.getPayId());
                if(1 == isopen){
                    throw new GlobeException(SystemConstants.FAIL_CODE, "支付通道已关闭,请选择其他通道,谢谢");
                }
                map = treasureServiceClient.insertRecord(vo.getAgentId(), vo.getUserId(), vo.getGameId(), vo.getPayId(),
                        vo.getOrderAmount(), vo.getRemarks(), vo.getAccount());
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
    public GlobeResponse getRebateInfo(Integer agentId, Integer userId) {
        if (agentId == null) {
            throw new GlobeException(SystemConstants.FAIL_CODE, "参数错误");
        }
        GlobeResponse<Object> globeResponse = new GlobeResponse<>();
        AgentRebateConfigVO agentRebateConfigVO = treasureServiceClient.getRebateInfo(agentId, userId);
        globeResponse.setData(agentRebateConfigVO);
        return globeResponse;
    }
}
