package com.lzkj.mobile.controller;

import com.lzkj.mobile.client.TreasureServiceClient;
import com.lzkj.mobile.vo.GlobeResponse;
import com.lzkj.mobile.vo.LuckyOrderConfigItemVO;
import com.lzkj.mobile.vo.LuckyOrderConfigVO;
import com.lzkj.mobile.vo.ThirdAppPayConfigVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

            configListRet.add(obj);
        });
        GlobeResponse<Object> globeResponse = new GlobeResponse<>();
        globeResponse.setData(configList);
        return globeResponse;
    }
}
