package com.lzkj.mobile.controller;

import com.lzkj.mobile.client.AgentServiceClient;
import com.lzkj.mobile.service.CheckUserSignatureService;
import com.lzkj.mobile.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.lzkj.mobile.util.MD5Utils.getAllFields;

@RestController
@RequestMapping("/qmPromotionInterface")
public class QmPromotionController {

    @Autowired
    private AgentServiceClient qmPromotionServiceClient;

    @Autowired
    @Lazy
    private CheckUserSignatureService checkUserSignatureService;

    /**
     * 查询全民推广奖励
     * @param userId
     * @return
     */
    @RequestMapping("/getDayPromotion")
    public GlobeResponse<Object> getDayPromotion(Integer userId){
        QmDayPromotionDetailVO list = qmPromotionServiceClient.getDayPromotion(userId);
        GlobeResponse<Object> globeResponse = new GlobeResponse<>();
        Map<String,Object> data = new HashMap<>();
        data.put("list",list);
        globeResponse.setData(data);
        return globeResponse;
    }

    /**
     * 用户奖励
     * @param userId
     * @return
     */
    @RequestMapping("getUserReward")
    public GlobeResponse<Object> getUserReward (Integer userId){
        QmUserRewardVO list = qmPromotionServiceClient.getUserReward(userId);
        GlobeResponse<Object> globeResponse = new GlobeResponse<>();
        Map<String,Object> data = new HashMap<>();
        data.put("list",list);
        globeResponse.setData(data);
        return globeResponse;
    }

     /**
     * 提取记录
     * @param request
     * @return
     */
    @RequestMapping("/getAppLiquidation")
    public GlobeResponse<Object> getAppLiquidation (HttpServletRequest request){
       String userIdParam = request.getParameter("userId");
       String pageIndexParam = request.getParameter("pageIndex");
       String pageSizeParam = request.getParameter("pageSize");
      //判空
       Integer pageIndex = pageIndexParam == null ? 1 : Integer.parseInt(pageIndexParam);
       Integer pageSize = pageSizeParam == null ? 10 : Integer.parseInt(pageSizeParam);
       Integer userId = userIdParam == null ? 0 : Integer.parseInt(userIdParam);

        //checkUserSignatureService.checkUserSignature(userId);
        QmLiquidationPageVo pageVo = qmPromotionServiceClient.getAppLiquidation(userId,pageIndex,pageSize);
        Map<String, Object> data = new HashMap<>(8);
        data.put("total",0);
        data.put("list",new ArrayList<>());
        List<QmLiquidationRewardRecordVO> list = new ArrayList<>();
        if (pageVo.getRecordCount() > 0) {
            pageVo.getQmLiquidationRewardRecordVO().stream().forEach(l->{
                QmLiquidationRewardRecordVO Liquidation = new QmLiquidationRewardRecordVO();
                getAllFields(l);
                Liquidation.setUserID(l.getUserID());
                Liquidation.setCreateDate(l.getCreateDate());
                Liquidation.setLiquidationReward(l.getLiquidationReward());
                Liquidation.setStatus(l.getStatus());
                if(l.getStatus() == 0){
                    Liquidation.setStatusWord("成功");
                }else{
                    Liquidation.setStatusWord("失败");
                }
                list.add(Liquidation);
            });
            data.put("total", pageVo.getRecordCount());
            data.put("list",list);
        }
            GlobeResponse<Object> globeResponse = new GlobeResponse<>();
            globeResponse.setData(data);
            return globeResponse;
    }

    /**
     * 推广明细
     * @param userId
     * @return
     */
    @RequestMapping("getPromotionDetails")
    public GlobeResponse<Object> getPromotionDetails (Integer userId){
        List<QmPromotionListVO> list = qmPromotionServiceClient.getPromotionDetails(userId);
        GlobeResponse<Object> globeResponse = new GlobeResponse<>();
        Map<String,Object> data = new HashMap<>();
        data.put("list",list);
        globeResponse.setData(data);
        return globeResponse;
    }

    /**
     * 推广周榜
     * @return
     */
    @RequestMapping("getWeekTopList")
    public GlobeResponse<Object> getWeekTopList(){
        List<QmWeekTopListVO> list = qmPromotionServiceClient.getWeekTopList();
        GlobeResponse<Object> globeResponse = new GlobeResponse<>();
        Map<String,Object> data = new HashMap<>();
        data.put("list",list);
        globeResponse.setData(data);
        return globeResponse;
    }
}
