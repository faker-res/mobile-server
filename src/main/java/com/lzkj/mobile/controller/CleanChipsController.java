package com.lzkj.mobile.controller;

import com.lzkj.mobile.client.PlatformServiceClient;
import com.lzkj.mobile.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/clean")
public class CleanChipsController {


    @Autowired
    private PlatformServiceClient platformServiceClient;

    /**
     * 1.视讯，2.电子，3.棋牌,4.捕鱼,5.体育,6.彩票
     * @param userId
     * @param agentId
     * @return
     */
    @RequestMapping("/chips")
    private GlobeResponse<Object> getCleanChips(Integer userId, Integer agentId) {
        //获取第三方除捕鱼外的数据
        CleanChipsTotalVO cleanChipsTotalVO  = platformServiceClient.getCleanChips(userId, agentId);
        GlobeResponse globeResponse = new GlobeResponse();
        globeResponse.setData(cleanChipsTotalVO);
        return globeResponse;
    }

    /**
     * 手动洗码
     * @param userId
     * @param agentId
     * @param vipLevel
     * @return
     */
    @RequestMapping("/washBet")
    private GlobeResponse<Object> washBet(Integer userId,Integer agentId,Integer vipLevel){
        if (vipLevel==0){
            vipLevel=1;
        }
        Boolean flag = platformServiceClient.washBet(userId,agentId,vipLevel);
        GlobeResponse globeResponse = new GlobeResponse();
        globeResponse.setData(flag);
        return globeResponse;
    }

    /**
     * 获取洗码记录
     */
    @RequestMapping("/getWashRecord")
    private GlobeResponse<Object> getWashRecord(Integer userId){
         List<CleanChipsRecordVO> list = platformServiceClient.getWashRecord(userId);
        GlobeResponse globeResponse = new GlobeResponse();
        globeResponse.setData(list);
        return globeResponse;
    }


    /**
     * 获取洗码记录详情
     */
    @RequestMapping("/getCleanChipsRecord")
    private GlobeResponse<Object> getCleanChipsRecord(Integer userId,String recordDate){
        List<CleanChipsRecordVO> list = platformServiceClient.getCleanChipsRecord(userId,recordDate);
        GlobeResponse globeResponse = new GlobeResponse();
        globeResponse.setData(list);
        return globeResponse;
    }

    /**
     * 洗码比列
     */
    @RequestMapping("/getCleanConfig")
    private GlobeResponse<Object> getCleanConfig(Integer agentId,int kindType){
        List<CleanChipsConfigVO> list =platformServiceClient.getCleanChipsConfig(agentId);
        List<ThirdKindConfigVO> getThirdConfig = platformServiceClient.getThirdConfig(kindType);

        Map data =new HashMap();
        data.put("vip",list);
        data.put("third",getThirdConfig);
        GlobeResponse globeResponse =new GlobeResponse();
        globeResponse.setData(data);
        return globeResponse;
    }
}
