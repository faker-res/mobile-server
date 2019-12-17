package com.lzkj.mobile.controller;

import com.alibaba.fastjson.JSONObject;
import com.lzkj.mobile.client.TreasureServiceClient;
import com.lzkj.mobile.config.SystemConstants;
import com.lzkj.mobile.exception.GlobeException;
import com.lzkj.mobile.vo.CommonPageVO;
import com.lzkj.mobile.vo.GlobeResponse;
import com.lzkj.mobile.vo.LuckyOrderInfoVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * 幸运注单
 */
@RestController
@RequestMapping("/luckyOrder")
@Slf4j
public class LuckyOrderController {
    @Autowired
    private TreasureServiceClient treasureServiceClient;

    /**
     * 查询幸运注单
     */
    @RequestMapping("/getLuckyOrderInfoList")
    public GlobeResponse<Object> getLuckyOrderInfoList(Integer pageIndex,Integer pageSize,Integer prizeState,Integer applyState,String startDate,String endDate,Integer userId){
        if( pageIndex == null || pageSize == null || userId==null ){
            throw new GlobeException(SystemConstants.FAIL_CODE, "参数错误");
        }
        prizeState = (prizeState==null?0:prizeState);
        applyState = (applyState==null?0:applyState);
        startDate = (startDate==null?"":startDate);
        endDate = (endDate==null?"":endDate);

        CommonPageVO<LuckyOrderInfoVO> list = treasureServiceClient.getLuckyOrderInfoList(pageIndex, pageSize,
                prizeState,  applyState,0, userId, startDate, endDate);
        Map<String, Object> data = new HashMap<>();
        data.put("list", list.getLists());
        data.put("total", list.getPageCount());
        GlobeResponse<Object> globeResponse = new GlobeResponse<>();
        globeResponse.setData(data);
        return globeResponse;
    }

    /**
     * 手动领奖
     */
    @RequestMapping("/receiveLuckyOrderInfo")
    public GlobeResponse<Object> receiveLuckyOrderInfo(@RequestBody LuckyOrderInfoVO vo){
        GlobeResponse<Object> globeResponse = new GlobeResponse<>();
        try{
            Boolean success = treasureServiceClient.receiveLuckyOrderInfo(vo);
            if(!success){
                globeResponse.setCode(SystemConstants.FAIL_CODE);
                globeResponse.setMsg("操作失败：未找到符合条件的数据");
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

    /**
     * 手动申请
     */
    @RequestMapping("/applyLuckyOrderInfo")
    public GlobeResponse<Object> applyLuckyOrderInfo(@RequestBody LuckyOrderInfoVO vo){
        GlobeResponse<Object> globeResponse = new GlobeResponse<>();
        try{
            Boolean success = treasureServiceClient.applyLuckyOrderInfo(vo);
            if(!success){
                globeResponse.setCode(SystemConstants.FAIL_CODE);
                globeResponse.setMsg("操作失败：未找到符合条件的数据");
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
