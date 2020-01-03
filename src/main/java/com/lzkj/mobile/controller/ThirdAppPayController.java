package com.lzkj.mobile.controller;

import com.lzkj.mobile.client.TreasureServiceClient;
import com.lzkj.mobile.config.SystemConstants;
import com.lzkj.mobile.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/thirdAppPay")
@Slf4j
public class ThirdAppPayController {
    @Autowired
    private TreasureServiceClient treasureServiceClient;

    /**
     * 查询业主的三方APP配置信息
     */
    @RequestMapping("/getLuckyOrderConfig")
    public GlobeResponse<Object> getLuckyOrderConfig(Integer agentId){
        List<ThirdAppPayConfigVO> configList = treasureServiceClient.getThirdAppPayConfigList(agentId);
        List<ThirdAppPayConfigVO> configListRet = new ArrayList<>();
        // 过滤掉不可用数据
        configList.stream().forEach(obj -> {
            if(new Integer(1).equals(obj.getEnableState())){
                configListRet.add(obj);
            }
        });
        GlobeResponse<Object> globeResponse = new GlobeResponse<>();
        globeResponse.setData(configList);
        return globeResponse;
    }

    @RequestMapping("/insertThirdPayRecord")
    public GlobeResponse<Object> insertThirdPayRecord(Integer gameId, Integer payType, String payName, BigDecimal orderAmount,
                                                      BigDecimal realAmount,String reserveMsg,String userAccount,Integer userId,Integer agentId){
        GlobeResponse<Object> globeResponse = new GlobeResponse<>();
        try{
            ThirdAppPayRecordVO vo = new ThirdAppPayRecordVO();
            vo.setUserId(userId);
            vo.setGameId(gameId);
            vo.setPayType(payType);
            vo.setPayName(payName);
            vo.setOrderAmount(orderAmount);
            vo.setRealAmount(realAmount);
            vo.setReserveMsg(reserveMsg);
            vo.setUserAccount(userAccount);
            vo.setAgentId(agentId);
            Boolean success = treasureServiceClient.insertThirdPayRecord(vo);
            if(!success){
                globeResponse.setCode(SystemConstants.FAIL_CODE);
                globeResponse.setMsg("操作失败：该奖励已失效或不满足领奖条件");
            }else{
                globeResponse.setCode(SystemConstants.SUCCESS_CODE);
                globeResponse.setMsg("保存成功");
            }
        }catch (Exception e){
            globeResponse.setCode(SystemConstants.FAIL_CODE);
            globeResponse.setMsg(e.getMessage());
        }
        return globeResponse;
    }
}
