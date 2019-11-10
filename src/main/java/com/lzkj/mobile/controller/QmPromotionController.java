package com.lzkj.mobile.controller;

import com.lzkj.mobile.client.AgentServiceClient;
import com.lzkj.mobile.config.SystemConstants;
import com.lzkj.mobile.exception.GlobeException;
import com.lzkj.mobile.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.lzkj.mobile.util.MD5Utils.getAllFields;
import static com.lzkj.mobile.util.TimeUtil.*;

@RestController
@RequestMapping("/qmPromotionInterface")
public class QmPromotionController {

    @Autowired
    private AgentServiceClient qmPromotionServiceClient;


    /**
     * 全民代理 -我的推广(首页信息)
     *
     * @param userId
     * @return
     */
    @RequestMapping("/getAgentMyPopularize")
    private GlobeResponse<Object> getAgentMyPopularize(Integer userId) {
        MyPopularizeVO agentSystemVO = qmPromotionServiceClient.getAgentMyPopularize(userId);
        GlobeResponse<Object> globeResponse = new GlobeResponse<>();
        globeResponse.setData(agentSystemVO);
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
     * 查询推广佣金配置
     *  1.视讯，2.电子，3.棋牌,4.捕鱼,5.体育,6.彩票
     */
    @RequestMapping("/zzSysRatio")
    private GlobeResponse<Object> getZzSysRatio(Integer agentId) {
        if (agentId==null||agentId ==0){
            throw new GlobeException(SystemConstants.FAIL_CODE, "参数错误!");
        }
        List<ZzSysRatioVO> list = qmPromotionServiceClient.getZzSysConfig(agentId);
        GlobeResponse<Object> globeResponse = new GlobeResponse<>();
        globeResponse.setData(list);
        return globeResponse;
    }

    /**
     * 直属玩家查询（gameid,昵称，今日流水，总流水，团队人数，直属人数）
     *
     * 查询全部： gameid 0,data ''
     * code：0:所有，1：今日，2：昨日，3本周，4，本月
     */
    @RequestMapping("/directQuery")
    private GlobeResponse<Object> getDirectQuery(Integer userId,Integer gameId,int code){
        String date =new String();
        switch (code){
            case 0:
                date="";
                break;
            case 1:
                date=getInitial();
                break;
            case 2:
                date=getYesterday();
                break;
            case 3:
                date =startWeek();
                break;
            case 4:
                date =startMonth();
                break;
        }
        List<QmDirectQueryVO> list = qmPromotionServiceClient.getDirectQuery(userId,gameId,date);

        GlobeResponse globeResponse =new GlobeResponse();
        globeResponse.setData(list);
        return globeResponse;
    }

    /**
     * 全民代理-直属玩家-推广详情
     */
    @RequestMapping("/directPromotionDetail")
    private GlobeResponse<Object> getDirectPromotionDetail(Integer userId){
        QmDayPromotionDetailVO qmDayPromotionDetailVO =qmPromotionServiceClient.getDirectPromotionDetail(userId);
        GlobeResponse globeResponse =new GlobeResponse();
        globeResponse.setData(qmDayPromotionDetailVO);
        return globeResponse;
    }

    /**
     * 全民代理-业绩查询
     *  useriD 当前玩家
     *  根据玩家gameId查询
     */
     @RequestMapping("/getDirectAchieve")
     private GlobeResponse<Object> getDirectAchieve(Integer userId,Integer gameId){
         List<QmPromotionDetailVO>list  = qmPromotionServiceClient.getDirectAchieve(userId,gameId);
         GlobeResponse globeResponse =new GlobeResponse();
         globeResponse.setData(list);
         return globeResponse;
     }

    /**
     * 全民代理-业绩来源
     */
    @RequestMapping("/getAchieveDetail")
    private GlobeResponse<Object> getAchieveDetail(Integer userId,Integer kindType){
        List<QmPromotionDetailVO>list  = qmPromotionServiceClient.getAchieveDetail(userId,kindType);
        GlobeResponse globeResponse =new GlobeResponse();
        globeResponse.setData(list);
        return globeResponse;
    }

//    /**
//     * 用户奖励
//     * @param userId
//     * @return
//     */
//    @RequestMapping("getUserReward")
//    public GlobeResponse<Object> getUserReward (Integer userId){
//        QmUserRewardVO list = qmPromotionServiceClient.getUserReward(userId);
//        GlobeResponse<Object> globeResponse = new GlobeResponse<>();
//        Map<String,Object> data = new HashMap<>();
//        data.put("list",list);
//        globeResponse.setData(data);
//        return globeResponse;
//    }
//
//
//
//    /**
//     * 推广明细
//     * @param userId
//     * @return
//     */
//    @RequestMapping("getPromotionDetails")
//    public GlobeResponse<Object> getPromotionDetails (Integer userId){
//        List<QmPromotionListVO> list = qmPromotionServiceClient.getPromotionDetails(userId);
//        GlobeResponse<Object> globeResponse = new GlobeResponse<>();
//        Map<String,Object> data = new HashMap<>();
//        data.put("list",list);
//        globeResponse.setData(data);
//        return globeResponse;
//    }

//    /**
//     * 推广周榜
//     * @return
//     */
//    @RequestMapping("getWeekTopList")
//    public GlobeResponse<Object> getWeekTopList(){
//        List<QmWeekTopListVO> list = qmPromotionServiceClient.getWeekTopList();
//        GlobeResponse<Object> globeResponse = new GlobeResponse<>();
//        Map<String,Object> data = new HashMap<>();
//        data.put("list",list);
//        globeResponse.setData(data);
//        return globeResponse;
//    }
}
