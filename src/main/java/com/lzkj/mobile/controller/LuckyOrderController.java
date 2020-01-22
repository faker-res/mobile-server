package com.lzkj.mobile.controller;

import com.lzkj.mobile.client.TreasureServiceClient;
import com.lzkj.mobile.config.SystemConstants;
import com.lzkj.mobile.exception.GlobeException;
import com.lzkj.mobile.redis.RedisKeyPrefix;
import com.lzkj.mobile.redis.RedisLock;
import com.lzkj.mobile.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    /**
     * 查询幸运注单配置
     */
    /*@RequestMapping("/getLuckyOrderConfigList")
    public GlobeResponse<Object>  getLuckyOrderConfigList(Integer pageIndex,Integer pageSize,Integer kindId,Integer kindType,
                                              BigDecimal betAmount,Integer joinTimeLimit,Integer numberMatchType,
                                              Integer awardType,Integer agentId){
        if( pageIndex == null || pageSize == null || agentId==null ){
            throw new GlobeException(SystemConstants.FAIL_CODE, "参数错误");
        }
        kindId = (kindId==null?0:kindId);
        kindType = (kindType==null?0:kindType);
        betAmount = (betAmount==null?BigDecimal.ZERO:betAmount);
        joinTimeLimit = (joinTimeLimit==null?0:joinTimeLimit);
        numberMatchType = (numberMatchType==null?0:numberMatchType);
        awardType = (awardType==null?0:awardType);
        CommonPageVO<LuckyOrderConfigVO> list = treasureServiceClient.getLuckyOrderConfigList( pageIndex,pageSize,
                kindId, kindType,betAmount,joinTimeLimit,numberMatchType,awardType,agentId);
        Map<String, Object> data = new HashMap<>();
        data.put("list", list.getLists());
        data.put("total", list.getPageCount());
        GlobeResponse<Object> globeResponse = new GlobeResponse<>();
        globeResponse.setData(data);
        return globeResponse;
    }*/

    /**
     * 根据业主查询配置
     * @return
     */
    @RequestMapping("/getLuckyOrderConfig")
    public GlobeResponse<Object> getLuckyOrderConfig(Integer agentId){
        LuckyOrderConfigVO vo = treasureServiceClient.getLuckyOrderConfig(agentId);
        // 只给客户端返回可用的配置
        if( vo == null || !(new Integer(1)).equals(vo.getEnableState()) ){
            vo = new LuckyOrderConfigVO();
        }else {
            List<LuckyOrderConfigItemVO> allItems = (vo.getItemList()==null?new ArrayList<>():vo.getItemList());
            List<LuckyOrderConfigItemVO> enableItems = new ArrayList<>();
            for(LuckyOrderConfigItemVO item : allItems){
                if( (new Integer(1)).equals(item.getEnableState()) ){
                    enableItems.add(item);
                }
            }
            vo.setItemList(enableItems);
        }
        GlobeResponse<Object> globeResponse = new GlobeResponse<>();
        globeResponse.setData(vo);
        return globeResponse;
    }


    /**
     * 查询幸运注单
     */
    @RequestMapping("/getLuckyOrderInfoList")
    public GlobeResponse<Object> getLuckyOrderInfoList(Integer pageIndex,Integer pageSize,Integer prizeState,Integer applyState,
                                                       String startDate,String endDate,Integer userId,
                                                       Integer kindId, Integer kindType,Integer gameId){
        if( pageIndex == null || pageSize == null || userId==null ){
            throw new GlobeException(SystemConstants.FAIL_CODE, "参数错误");
        }
        prizeState = (prizeState==null?0:prizeState);
        applyState = (applyState==null?0:applyState);
        startDate = (startDate==null?"":startDate);
        endDate = (endDate==null?"":endDate);
        kindId = (kindId==null?0:kindId);
        kindType = (kindType==null?0:kindType);
        gameId = (gameId==null?0:gameId);

        CommonPageVO<LuckyOrderInfoVO> list = treasureServiceClient.getLuckyOrderInfoList(pageIndex, pageSize,
                prizeState,  applyState,0, userId, startDate, endDate,kindId,kindType,gameId);
        Map<String, Object> data = new HashMap<>(100);
        if(list==null){
            data.put("list", new ArrayList());
            data.put("total", 0);
        }else{
            data.put("list", list.getLists());
            data.put("total", list.getRecordCount());
        }

        GlobeResponse<Object> globeResponse = new GlobeResponse<>();
        globeResponse.setData(data);
        return globeResponse;
    }

    /**
     * 幸运注单明细
     *
     * @param param
     * @return
     */
    @RequestMapping("/luckyOrderDetailList")
    public GlobeResponse<Object> luckyOrderDetailList(Map<String, Object> param) {
        CommonPageVO<LuckyOrderDetailVo> pageVO = treasureServiceClient.luckyOrderDetailList(param);
        Map<String, Object> data = new HashMap<>(100);
        if(pageVO == null){
            data.put("list", new ArrayList());
            data.put("total", 0);
        }else{
            data.put("list", pageVO.getLists());
            data.put("total", pageVO.getRecordCount());
        }
        GlobeResponse<Object> globeResponse = new GlobeResponse<>();
        globeResponse.setData(data);
        return globeResponse;
    }

    /**
     * 手动领奖
     */
    @RequestMapping("/receiveLuckyOrderInfo")
    public GlobeResponse<Object> receiveLuckyOrderInfo(Integer userId,Integer id){
        GlobeResponse<Object> globeResponse = new GlobeResponse<>();
        RedisLock redisLock = null;
        try{
            redisLock = new RedisLock(RedisKeyPrefix.payLock("userBalance_"+userId), redisTemplate, 10);
            Boolean hasLock = redisLock.tryLock();
            if (!hasLock) {
                globeResponse.setCode(SystemConstants.FAIL_CODE);
                globeResponse.setMsg("操作失败：请求太频繁，请稍后重试");
                return globeResponse;
            }
            LuckyOrderInfoVO vo = new LuckyOrderInfoVO();
            vo.setUserId(userId);
            vo.setId(id);
            Boolean success = treasureServiceClient.receiveLuckyOrderInfo(vo);
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
        }finally {
            if( redisLock != null ){
                redisLock.unlock();
            }
        }
        return globeResponse;
    }

    /**
     * 手动申请
     */
    @RequestMapping("/applyLuckyOrderInfo")
    public GlobeResponse<Object> applyLuckyOrderInfo(Integer userId,Integer id){
        GlobeResponse<Object> globeResponse = new GlobeResponse<>();
        try{
            LuckyOrderInfoVO vo = new LuckyOrderInfoVO();
            vo.setUserId(userId);
            vo.setId(id);
            Boolean success = treasureServiceClient.applyLuckyOrderInfo(vo);
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
