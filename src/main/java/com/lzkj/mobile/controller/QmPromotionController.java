package com.lzkj.mobile.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lzkj.mobile.client.AccountsServiceClient;
import com.lzkj.mobile.client.AgentServiceClient;
import com.lzkj.mobile.config.SystemConstants;
import com.lzkj.mobile.exception.GlobeException;
import com.lzkj.mobile.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.lzkj.mobile.util.MD5Utils.getAllFields;
import static com.lzkj.mobile.util.TimeUtil.*;

/**
 * 推广赚钱
 *
 * @author xxx
 */
@RestController
@RequestMapping("/qmPromotionInterface")
public class QmPromotionController {

    @Autowired
    private AgentServiceClient qmPromotionServiceClient;

    @Autowired
    private AccountsServiceClient accountsServiceClient;


    /**
     * 全民代理 -我的推广(首页信息)
     *
     * @param userId
     * @return
     */
    @RequestMapping("/getAgentMyPopularize")
    public GlobeResponse<Object> getAgentMyPopularize(Integer userId) {
        MyPopularizeVO agentSystemVO = qmPromotionServiceClient.getAgentMyPopularize(userId);
        GlobeResponse<Object> globeResponse = new GlobeResponse<>();
        globeResponse.setData(agentSystemVO);
        return globeResponse;
    }

    /**
     * 领取记录
     *
     * @param request
     * @return
     */
    @RequestMapping("/getAppLiquidation")
    public GlobeResponse<Object> getAppLiquidation(HttpServletRequest request) {
        String userIdParam = request.getParameter("userId");
        String pageIndexParam = request.getParameter("pageIndex");
        String pageSizeParam = request.getParameter("pageSize");
        //判空
        Integer pageIndex = pageIndexParam == null ? 1 : Integer.parseInt(pageIndexParam);
        Integer pageSize = pageSizeParam == null ? 10 : Integer.parseInt(pageSizeParam);
        Integer userId = userIdParam == null ? 0 : Integer.parseInt(userIdParam);
        //查询领取记录
        QmLiquidationPageVo pageVo = qmPromotionServiceClient.getAppLiquidation(userId, pageIndex, pageSize);
        Map<String, Object> data = new HashMap<>(8);
        data.put("total", 0);
        data.put("list", new ArrayList<>());
        List<QmLiquidationRewardRecordVO> list = new ArrayList<>();
        if (pageVo.getRecordCount() > 0) {
            pageVo.getQmLiquidationRewardRecordVO().stream().forEach(l -> {
                QmLiquidationRewardRecordVO Liquidation = new QmLiquidationRewardRecordVO();
                getAllFields(l);
                Liquidation.setUserID(l.getUserID());
                Liquidation.setCreateDate(l.getCreateDate());
                Liquidation.setLiquidationReward(l.getLiquidationReward());
                Liquidation.setStatus(l.getStatus());
                if (l.getStatus() == 0) {
                    Liquidation.setStatusWord("成功");
                } else {
                    Liquidation.setStatusWord("失败");
                }
                list.add(Liquidation);
            });
            data.put("total", pageVo.getRecordCount());
            data.put("list", list);
        }
        GlobeResponse<Object> globeResponse = new GlobeResponse<>();
        globeResponse.setData(data);
        return globeResponse;
    }

    /**
     * 查询推广佣金配置
     *
     * 1.视讯 2.电子 3.棋牌 4.捕鱼 5.体育 6.彩票
     *
     * @param agentId
     * @return
     */
    @RequestMapping("/zzSysRatio")
    public GlobeResponse<Object> getZzSysRatio(Integer agentId) {
        if (agentId == null || agentId == 0) {
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
     * <p>
     * 查询全部： gameid 0,data ''
     *
     * code：0:所有，1：今日，2：昨日，3本周，4，本月
     *
     * @param userId
     * @param gameId
     * @param page
     * @return
     */
    @RequestMapping("/directQuery")
    public GlobeResponse<Object> getDirectQuery(Integer userId, Integer gameId, int code,int page) {
        String date = new String();
        switch (code) {
            case 0:
                date = "";
                break;
            case 1:
                date = getInitial();
                break;
            case 2:
                date = getYesterday();
                break;
            case 3:
                date = startWeek();
                break;
            case 4:
                date = startMonth();
                break;
        }
        List<QmDirectQueryVO> list = qmPromotionServiceClient.getDirectQuery(userId, gameId, date,page);
        MyPopularizeVO myPopularizeVO = qmPromotionServiceClient.getTeamMember(userId);
        Map<String, Object> map = new HashMap<>(5);
        map.put("list", list);
        map.put("directlyMember", myPopularizeVO.getDirectlyMemberCount());
        map.put("directlyAgent", myPopularizeVO.getDirectlyAgent());
        map.put("todayTeamBet", myPopularizeVO.getTodayTeamBet());
        map.put("todayDirectlyBet", myPopularizeVO.getTodayDirectlyBet());
        GlobeResponse globeResponse = new GlobeResponse();
        globeResponse.setData(map);
        return globeResponse;
    }

    /**
     * 全民代理-直属玩家-直属玩家详情
     *
     * @param userId
     * @return
     */
    @RequestMapping("/directPromotionDetail")
    public GlobeResponse<Object> getDirectPromotionDetail(Integer userId) {
        QmDayPromotionDetailVO qmDayPromotionDetailVO = qmPromotionServiceClient.getDirectPromotionDetail(userId);
        GlobeResponse globeResponse = new GlobeResponse();
        globeResponse.setData(qmDayPromotionDetailVO);
        return globeResponse;
    }

    /**
     * 全民代理-业绩查询
     *
     * useriD 当前玩家
     *
     * 根据玩家gameId查询
     *
     * @param userId
     * @param gameId
     * @return
     */
    @RequestMapping("/getDirectAchieve")
    public GlobeResponse<Object> getDirectAchieve(Integer userId, Integer gameId) {
        List<QmPromotionDetailVO> list = qmPromotionServiceClient.getDirectAchieve(userId, gameId);
        GlobeResponse globeResponse = new GlobeResponse();
        globeResponse.setData(list);
        return globeResponse;
    }

    /**
     * 全民代理-业绩来源
     *
     * @param userId
     * @param kindType
     * @return
     */
    @RequestMapping("/getAchieveDetail")
    public GlobeResponse<Object> getAchieveDetail(Integer userId, Integer kindType) {
        List<QmPromotionDetailVO> list = qmPromotionServiceClient.getAchieveDetail(userId, kindType);
        GlobeResponse globeResponse = new GlobeResponse();
        globeResponse.setData(list);
        return globeResponse;
    }

    /**
     *  全民-领取佣金
     *
     * @param userId
     * @return
     */
    @RequestMapping("/receiveCommission")
    public GlobeResponse<Object> receiveCommission(Integer userId) {
        BigDecimal score = qmPromotionServiceClient.receiveCommission(userId);
        GlobeResponse globeResponse = new GlobeResponse();
        globeResponse.setData(score);
        return globeResponse;
    }

    /**
     * 获取该玩家的保底返佣
     *
     * @param gameId
     * @return
     */
     @RequestMapping("/getGuaranteedRatio")
     public  GlobeResponse<Object> getGuaranteedRatio(Integer gameId)  {
         BigDecimal userRation = accountsServiceClient.queryRatioUserInfo(gameId);
         GlobeResponse<Object> globeResponse = new GlobeResponse<>();
         globeResponse.setData(userRation.multiply(new BigDecimal(10000)));
         return globeResponse;
     }

    /**
     * 保底返佣设置
     *
     * @param gameId
     * @param ratio
     * @return
     */
    @RequestMapping("/editRatio")
    public GlobeResponse<Object> editRatio(Integer gameId, BigDecimal ratio) throws ParseException {

        ratio = ratio.divide(new BigDecimal(10000), 4, BigDecimal.ROUND_DOWN);
        //获取上级代理返佣比例
        BigDecimal parentRation = accountsServiceClient.queryParentRation(gameId);
        if (parentRation == null) {
            throw new GlobeException(SystemConstants.FAIL_CODE, "没有找到上级玩家");
        }
        //验证返佣不允许大于上级代理
        if (ratio.compareTo(parentRation) == 1 || ratio.compareTo(parentRation) == 0) {
            throw new GlobeException(SystemConstants.FAIL_CODE, "返佣比例不可超过或等同上级代理!");
        }
        //查询当前用户的返佣比例
        BigDecimal userRation = accountsServiceClient.queryRatioUserInfo(gameId);
        if (userRation.compareTo(BigDecimal.ZERO) == 1) {
            if (ratio.compareTo(userRation) == -1) {
                throw new GlobeException(SystemConstants.FAIL_CODE,
                        "本次设置每万元返佣额不能小于原有每万元返佣额, 原有的每万元返佣额为：" + userRation.multiply(new BigDecimal(10000)).stripTrailingZeros());
            }
        }
        GlobeResponse<Object> globeResponse = new GlobeResponse<>();
        accountsServiceClient.editRatio(ratio, gameId);
        globeResponse.setCode(SystemConstants.SUCCESS_CODE);
        globeResponse.setMsg("保存成功");
        return globeResponse;
    }
}
