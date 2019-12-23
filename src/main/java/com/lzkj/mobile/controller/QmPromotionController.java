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
    private GlobeResponse<Object> getAgentMyPopularize(Integer userId) {
        MyPopularizeVO agentSystemVO = qmPromotionServiceClient.getAgentMyPopularize(userId);
        GlobeResponse<Object> globeResponse = new GlobeResponse<>();
        globeResponse.setData(agentSystemVO);
        return globeResponse;
    }

    /**
     * 提取记录
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
     * 1.视讯，2.电子，3.棋牌,4.捕鱼,5.体育,6.彩票
     */
    @RequestMapping("/zzSysRatio")
    private GlobeResponse<Object> getZzSysRatio(Integer agentId) {
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
     * <p>
     * 查询全部： gameid 0,data ''
     * code：0:所有，1：今日，2：昨日，3本周，4，本月
     */
    @RequestMapping("/directQuery")
    private GlobeResponse<Object> getDirectQuery(Integer userId, Integer gameId, int code,int page) {
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
        Map<String,Object> map =new HashMap<>();
        map.put("list",list);
        map.put("directlyMember",myPopularizeVO.getDirectlyMemberCount());
        map.put("directlyAgent",myPopularizeVO.getDirectlyAgent());
        map.put("todayTeamBet",myPopularizeVO.getTodayTeamBet());
        GlobeResponse globeResponse = new GlobeResponse();
        globeResponse.setData(map);
        return globeResponse;
    }

    /**
     * 全民代理-直属玩家-直属玩家详情
     */
    @RequestMapping("/directPromotionDetail")
    private GlobeResponse<Object> getDirectPromotionDetail(Integer userId) {
        QmDayPromotionDetailVO qmDayPromotionDetailVO = qmPromotionServiceClient.getDirectPromotionDetail(userId);
        GlobeResponse globeResponse = new GlobeResponse();
        globeResponse.setData(qmDayPromotionDetailVO);
        return globeResponse;
    }

    /**
     * 全民代理-业绩查询
     * useriD 当前玩家
     * 根据玩家gameId查询
     */
    @RequestMapping("/getDirectAchieve")
    private GlobeResponse<Object> getDirectAchieve(Integer userId, Integer gameId) {
        List<QmPromotionDetailVO> list = qmPromotionServiceClient.getDirectAchieve(userId, gameId);
        GlobeResponse globeResponse = new GlobeResponse();
        globeResponse.setData(list);
        return globeResponse;
    }

    /**
     * 全民代理-业绩来源
     */
    @RequestMapping("/getAchieveDetail")
    private GlobeResponse<Object> getAchieveDetail(Integer userId, Integer kindType) {
        List<QmPromotionDetailVO> list = qmPromotionServiceClient.getAchieveDetail(userId, kindType);
        GlobeResponse globeResponse = new GlobeResponse();
        globeResponse.setData(list);
        return globeResponse;
    }

    /**
     *  全民-领取佣金
     * @param userId
     * @return
     */
    @RequestMapping("/receiveCommission")
    private GlobeResponse<Object> receiveCommission(Integer userId) {
        BigDecimal score = qmPromotionServiceClient.receiveCommission(userId);
        GlobeResponse globeResponse = new GlobeResponse();
        globeResponse.setData(score);
        return globeResponse;
    }

    /**
     * 获取该玩家的保底返佣
     */
     @RequestMapping("/getGuaranteedRatio")
     private  GlobeResponse<AccountsQmRatioVO> getGuaranteedRatio(Integer gameId)  {
         //默认棋牌配置
         AccountsQmRatioVO userRation = accountsServiceClient.queryRatioUserInfo(gameId);
         GlobeResponse<AccountsQmRatioVO> globeResponse = new GlobeResponse<>();
         globeResponse.setData(userRation);
         return globeResponse;
     }

    /**
     * 保底返佣设置,根据kindType 获取返佣配置
     */
    @RequestMapping("/editRatio")
    private GlobeResponse<Object> editRatio(String rebatesVO) throws ParseException {
        JSONArray jsonArray = JSONArray.parseArray(rebatesVO);
        GlobeResponse<Object> globeResponse = new GlobeResponse<>();
        List<GuaranteedRebatesVO> objects = new ArrayList<>();
        for (Object vo : jsonArray) {
            GuaranteedRebatesVO guaranteedRebatesVO = new GuaranteedRebatesVO();
            JSONObject dJson = JSONObject.parseObject(vo.toString());
            guaranteedRebatesVO.setGameId(dJson.getInteger("gameId"));
            guaranteedRebatesVO.setKindType(dJson.getInteger("kindType"));
            guaranteedRebatesVO.setRatio(dJson.getBigDecimal("ratio"));
            objects.add(guaranteedRebatesVO);
        }
        if (objects != null && objects.size() > 0) {
            for (GuaranteedRebatesVO vo : objects) {
               BigDecimal ratio = vo.getRatio().divide(new BigDecimal(10000), 4, BigDecimal.ROUND_DOWN);
                //获取上级代理返佣比例
                BigDecimal parentRation = accountsServiceClient.queryParentRation(vo.getGameId(),vo.getKindType() );
                if (parentRation == null) {
                    throw new GlobeException(SystemConstants.FAIL_CODE, vo.getKindType() + "没有找到上级玩家");
                }
//        //当前保底值设置不能超过判定税收
//        if(new BigDecimal(0.025).compareTo(ratio) == -1){
//            throw new GlobeException(SystemConstants.FAIL_CODE, "当前保底值设置不能超过绑定税收!");
//        }
                //验证返佣不允许大于上级代理
                if (ratio.compareTo(parentRation) == 1 || ratio.compareTo(parentRation) == 0) {
                    throw new GlobeException(SystemConstants.FAIL_CODE, vo.getKindType() + "返佣比例不可超过或等同上级代理!");
                }
                //获取设置当前用户的返佣比例
                BigDecimal userRation = accountsServiceClient.queryRatioUserInfoType(vo.getGameId(),vo.getKindType());
                if (userRation.compareTo(BigDecimal.ZERO) == 1) {
                    if (ratio.compareTo(userRation) == -1) {
                        //DecimalFormat df = new DecimalFormat("0.00%");
                        throw new GlobeException(SystemConstants.FAIL_CODE, vo.getKindType() + "本次设置返佣比例不能小于原有返佣比例,原有的返佣比例为：" + userRation.multiply(new BigDecimal(10000)).stripTrailingZeros());
                    }
                }
                vo.setRatio(ratio);
            }
        }
           Boolean falg = accountsServiceClient.editRatio(objects);
           if (!falg) {
               globeResponse.setCode(SystemConstants.FAIL_CODE);
               globeResponse.setMsg("保存失败");
               return globeResponse;
           }
        globeResponse.setCode(SystemConstants.SUCCESS_CODE);
        globeResponse.setMsg("保存成功");

        return globeResponse;
    }
}
