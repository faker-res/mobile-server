package com.lzkj.mobile.controller;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lzkj.mobile.client.AccountsServiceClient;
import com.lzkj.mobile.client.AgentServiceClient;
import com.lzkj.mobile.client.PlatformServiceClient;
import com.lzkj.mobile.client.TreasureServiceClient;
import com.lzkj.mobile.config.AgentSystemEnum;
import com.lzkj.mobile.config.SystemConstants;
import com.lzkj.mobile.exception.GlobeException;
import com.lzkj.mobile.vo.AccReportVO;
import com.lzkj.mobile.vo.AgentAccVO;
import com.lzkj.mobile.vo.AgentSystemStatusInfoVO;
import com.lzkj.mobile.vo.BankInfoVO;
import com.lzkj.mobile.vo.CloudShieldConfigurationVO;
import com.lzkj.mobile.vo.DayRankingRewardVO;
import com.lzkj.mobile.vo.DayUserAbsScoreVO;
import com.lzkj.mobile.vo.GlobeResponse;
import com.lzkj.mobile.vo.MyPlayerVO;
import com.lzkj.mobile.vo.MyPopularizeVO;
import com.lzkj.mobile.vo.MyQmTxRecord;
import com.lzkj.mobile.vo.MyRewardRecordVO;
import com.lzkj.mobile.vo.MyRewardVO;
import com.lzkj.mobile.vo.QmAchievementVO;
import com.lzkj.mobile.vo.RankingListVO;
import com.lzkj.mobile.vo.RankingListVO.Ranking;
import com.lzkj.mobile.vo.SystemStatusInfoVO;
import com.lzkj.mobile.vo.YebConfigVO;
import com.lzkj.mobile.vo.ZzSysRatioVO;
import com.lzkj.mobile.vo.yebProfitDetailsVO;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/agentSystem")
@Slf4j
public class AgentSystemController {

    @Autowired
    private AgentServiceClient agenteClient;

    @Autowired
    private AccountsServiceClient accountsClient;

    @Autowired
    private TreasureServiceClient treasureServiceClient;

    @Autowired
    private PlatformServiceClient platformServiceClient;

    /**
     * 代理系统-我的推广
     *
     * @param userId
     * @return
     */
    @RequestMapping("/getAgentMyPopularize")
    private GlobeResponse<Object> getAgentMyPopularize(Integer userId) {
        MyPopularizeVO agentSystemVO = agenteClient.getAgentMyPopularize(userId);
        GlobeResponse<Object> globeResponse = new GlobeResponse<>();
        globeResponse.setData(agentSystemVO);
        return globeResponse;
    }

    /**
     * 代理系统-我的玩家
     *
     * @param userId
     * @param memberId
     * @return
     */
    @RequestMapping("/getAgentMyPlayer")
    private GlobeResponse getAgentMyPlayer(Integer userId, Integer memberId, Integer pageIndex) {
        if (memberId == null) memberId = 0;
        List<MyPlayerVO> list = agenteClient.getAgentMyPlayer(userId, memberId, pageIndex);
        Integer count = accountsClient.getMyDirectlyPlayer(userId);
        if (count == null) {
            count = 0;
        }
        List<MyPlayerVO> lists = new ArrayList<MyPlayerVO>();
        if (list.size() > 0) {
            list.forEach(obj -> {
                MyPlayerVO vo = obj;
                vo.setZzQmRatio(obj.getZzQmRatio().multiply(new BigDecimal(10000)).stripTrailingZeros());
                lists.add(vo);
            });
        }
        Map<String, Object> param = new HashMap<>();
        param.put("total", count);
        param.put("list", lists);
        GlobeResponse globeResponse = new GlobeResponse<>();
        globeResponse.setData(param);
        return globeResponse;
    }

    /**
     * 代理系统-我的玩家
     *
     * @param userId
     * @return
     */
    @RequestMapping("/getAgentMyTeam")
    private GlobeResponse<List<MyPlayerVO>> getAgentMyPlayer(Integer userId, Integer agentId, Integer pageSize, Integer pageIndex) {

        List<MyPlayerVO> list = agenteClient.getAgentMyTeam(userId, agentId, pageSize, pageIndex);
        log.info("全民代理数据{}", list);
        List<MyPlayerVO> lists = new ArrayList<MyPlayerVO>();
        if (list.size() > 0) {
            list.forEach(obj -> {
                MyPlayerVO vo = obj;
                vo.setZzQmRatio(obj.getZzQmRatio().multiply(new BigDecimal(10000)).stripTrailingZeros());
                lists.add(vo);
            });
        }
        GlobeResponse<List<MyPlayerVO>> globeResponse = new GlobeResponse<>();
        globeResponse.setData(lists);
        return globeResponse;
    }

    /**
     * 推广-我的业绩
     */
    @RequestMapping("/getAchievement")
    private GlobeResponse<Object> getAchievement(Integer userId) {
        List<QmAchievementVO> list = agenteClient.getAchievement(userId);
        BigDecimal weekTotalAchievement = BigDecimal.ZERO;
        BigDecimal weekTeamAchievement = BigDecimal.ZERO;
        BigDecimal weekPersonalAchievement = BigDecimal.ZERO;
        BigDecimal estimatedRevenue = BigDecimal.ZERO;
        BigDecimal dayRevenue = BigDecimal.ZERO;
        for (QmAchievementVO qmAchievement : list) {
            estimatedRevenue = qmAchievement.getEstimatedRevenue();
            dayRevenue = qmAchievement.getDayRevenue();
            weekTotalAchievement = weekTotalAchievement.add(qmAchievement.getDayTotalAchievement());
            weekTeamAchievement = weekTeamAchievement.add(qmAchievement.getDayTeamAchievement());
            weekPersonalAchievement = weekPersonalAchievement.add(qmAchievement.getDayPersonalAchievement());
        }

        HashMap<String, Object> data = new HashMap<>();
        data.put("weekTotalAchievement", weekTotalAchievement);
        data.put("weekTeamAchievement", weekTeamAchievement);
        data.put("weekPersonalAchievement", weekPersonalAchievement);
        data.put("estimatedRevenue", estimatedRevenue);
        data.put("dayRevenue", dayRevenue);
        data.put("list", list);
        GlobeResponse<Object> globeResponse = new GlobeResponse<>();
        globeResponse.setData(data);
        return globeResponse;
    }

    /**
     * 推广-我的奖励
     */
    @RequestMapping("/getMyRewardRecord")
    private GlobeResponse<Object> getMyRewardRecord(Integer userId) {
        List<MyRewardRecordVO> list = agenteClient.getMyRewardRecord(userId);
        GlobeResponse<Object> globeResponse = new GlobeResponse<>();
        globeResponse.setData(list);
        return globeResponse;
    }

    /**
     * 推广-我的提现
     */
    @RequestMapping("/myTxRecord")
    private GlobeResponse<Object> getMyTxRecord(Integer userId, Integer pageSize, Integer pageIndex) {
        if (pageSize == null || pageSize > 20) pageSize = 50;
        if (pageIndex == null || pageSize < 1) pageIndex = 1;
        List<MyQmTxRecord> list = agenteClient.getMyQmTxRecord(userId, pageSize, pageIndex);
        MyRewardVO data = agenteClient.getMyReward(userId);
        if (list.size() > 0) {
            data.setList(list);
        }

        GlobeResponse<Object> globeResponse = new GlobeResponse<>();
        globeResponse.setData(data);
        return globeResponse;
    }

    /**
     * 保底返佣设置
     */
    @RequestMapping("/editRatio")
    private GlobeResponse<Object> editRatio(Integer gameId, BigDecimal ratio) throws ParseException {

        ratio = ratio.divide(new BigDecimal(10000), 4, BigDecimal.ROUND_DOWN);
        //获取上级代理返佣比例
        BigDecimal parentRation = accountsClient.queryParentRation(gameId);
        if (parentRation == null) {
            throw new GlobeException(SystemConstants.FAIL_CODE, "没有找到上级玩家");
        }
//        //当前保底值设置不能超过判定税收
//        if(new BigDecimal(0.025).compareTo(ratio) == -1){
//            throw new GlobeException(SystemConstants.FAIL_CODE, "当前保底值设置不能超过绑定税收!");
//        }
        //验证返佣不允许大于上级代理
        if (ratio.compareTo(parentRation) == 1 || ratio.compareTo(parentRation) == 0) {
            throw new GlobeException(SystemConstants.FAIL_CODE, "返佣比例不可超过或等同上级代理!");
        }
        //获取设置当前用户的返佣比例
        BigDecimal userRation = accountsClient.queryRatioUserInfo(gameId);
        if (userRation.compareTo(BigDecimal.ZERO) == 1) {
            if (ratio.compareTo(userRation) == -1) {
                //DecimalFormat df = new DecimalFormat("0.00%");
                throw new GlobeException(SystemConstants.FAIL_CODE, "本次设置返佣比例不能小于原有返佣比例,原有的返佣比例为：" + userRation.multiply(new BigDecimal(10000)).stripTrailingZeros());
            }
        }
        GlobeResponse<Object> globeResponse = new GlobeResponse<>();

        accountsClient.editRatio(ratio, gameId);
        globeResponse.setCode(SystemConstants.SUCCESS_CODE);
        globeResponse.setMsg("保存成功");

        return globeResponse;
    }

    /**
     * 查询推广佣金
     */
    @RequestMapping("/zzSysRatio")
    private GlobeResponse<Object> getZzSysRatio() {
        List<ZzSysRatioVO> list = agenteClient.getZzSysRatio();
        GlobeResponse<Object> globeResponse = new GlobeResponse<>();
        globeResponse.setData(list);
        return globeResponse;
    }

    /**
     * 登录检测是否维护的
     *
     * @return
     */
    @RequestMapping("/isMaintain")
    public GlobeResponse<Object> getMaintainKey() {
        String key = "EnjoinLogon";
        SystemStatusInfoVO systemStatusInfo = accountsClient.getSystemStatusInfo(key);
        Boolean flag = true;
        if (systemStatusInfo.getStatusValue().compareTo(BigDecimal.ZERO) != 0) {
            flag = false;
        }
        GlobeResponse<Object> globeResponse = new GlobeResponse<>();
        globeResponse.setData(flag);
        return globeResponse;
    }

    /**
     * 获取推广连接与版本号
     */
    @RequestMapping("/getQrCode")
    public Map<String, Object> getQrcode(Integer agentId) {
        if (null == agentId || agentId == 0) {
            throw new GlobeException(SystemConstants.FAIL_CODE, "代理ID不能为空!");
        }
        AgentAccVO agentAccVO = agenteClient.getQrCode(agentId);
        Map<String, Object> data = new HashMap<>();
        data.put("QRcode", agentAccVO.getAgentUrl());
        data.put("VERSION_APK", agentAccVO.getAgentVersion());
        return data;
    }

    /**
     * 获取银行卡列表
     */
    @RequestMapping("/getBankInfo")
    public GlobeResponse<Object> getMaintainKey(Integer agentId) {
        if (null == agentId || agentId == 0) {
            throw new GlobeException(SystemConstants.FAIL_CODE, "参数错误!");
        }
        List<BankInfoVO> bankInfos = platformServiceClient.getBankList(agentId);
        GlobeResponse<Object> globeResponse = new GlobeResponse<>();
        globeResponse.setData(bankInfos);
        return globeResponse;
    }

    /**
     * 获取二维码和热更地址
     *
     * @param agentId
     * @return
     */
    @RequestMapping("/loginStatus")
    public Map<String, Object> getLoginStatus(Integer agentId, String registerMachine) {
        if (null == agentId || agentId == 0) {
            throw new GlobeException(SystemConstants.FAIL_CODE, "参数错误!");
        }

        AgentAccVO agentAccVO = agenteClient.getQrCode(agentId);
        String key = "EnjoinLogon";
        //总控的维护
        SystemStatusInfoVO systemStatusInfo = accountsClient.getSystemStatusInfo(key);
        Boolean flag = false;
        if (systemStatusInfo.getStatusValue().compareTo(BigDecimal.ZERO) != 0) {
            flag = true;
        }
        Map<String, Object> data = new HashMap<>();
        //判断预更新热更开关開啓沒
        if ("0".equals(String.valueOf(agentAccVO.getStatus()))) {
            String[] update = agentAccVO.getUpdateAddress().split(",");
            data.put("HOT_UPDATE_URL", update);
        }else {
            //验证是否有机器码
            if (!StringUtils.isBlank(registerMachine)) {
                int num = platformServiceClient.getWhitelist(registerMachine);
                if (num > 0) {
                    String[] preUpdateAddress = agentAccVO.getPreUpdateAddress().split(",");
                    data.put("preUpdateAddress", preUpdateAddress);
                    flag = false;
                } else {
                    String[] update = agentAccVO.getUpdateAddress().split(",");
                    data.put("HOT_UPDATE_URL", update);
                }
            }
        }
        //获取业主配置
        List<AgentSystemStatusInfoVO> agentSystemList = agenteClient.getBindMobileSendInfo(agentId);
        data.put("QRcode", agentAccVO.getAgentUrl());
        data.put("VERSION_APK", agentAccVO.getAgentVersion());
        data.put("Maitance", flag);
        data.put("ClientUrl", agentAccVO.getClientUrl());
        data.put("prompt", agentAccVO.getPrompt());
        for (AgentSystemStatusInfoVO vo : agentSystemList) {
            //绑定手机
            if (vo.getStatusName().equals(AgentSystemEnum.BindMobileSend.getName())) {
                data.put("bindMobileSend", vo.getStatusValue());
            }
            //注册填写真实姓名
            if (vo.getStatusName().equals(AgentSystemEnum.ShowRealName.getName())) {
                if (vo.getIsShow() == 1) {
                    data.put("ShowRealName", false);
                } else {
                    data.put("ShowRealName", true);
                }
            }
            //如果总控没有维护,并且业主维护的时候
            if (!flag) {
                if (vo.getStatusName().equals(AgentSystemEnum.EnjoinLogon.getName())) {
                    if (vo.getStatusValue().compareTo(BigDecimal.ZERO) != 0) {
                        data.put("Maitance", true);
                    }
                }
            }
            if (vo.getStatusName().equals(AgentSystemEnum.VIPOpen.getName())) {
                if (vo.getStatusValue().compareTo(BigDecimal.ZERO) == 0) {
                    data.put("vip", true);
                } else {
                    data.put("vip", false);
                }
            }
        }
        //余额宝是否开启
        YebConfigVO yebConfigVO = treasureServiceClient.getYebIsOpen(agentId);
        if (yebConfigVO.getIsOpen() == 0) {
            data.put("yebiIsopen", false);
            data.put("description", yebConfigVO.getDescription());
        } else {
            data.put("yebiIsopen", true);
            data.put("description", yebConfigVO.getDescription());
        }
        List<CloudShieldConfigurationVO> vo = agenteClient.getCloudShieldConfigurationInfos(agentId);
        if(vo != null) {
        	data.put("CloudData", vo);
        }
        return data;
    }

    /**
     * 更新玩家IP
     */
    @RequestMapping("/updateIp")
    public GlobeResponse<Object> updateIp(Integer userId, String ip, String phoneType, String loginArea) {
        if (StringUtils.isBlank(ip)) {
            throw new GlobeException(SystemConstants.FAIL_CODE, "获取IP为空!");
        }
        if (StringUtils.isBlank(phoneType)) {
            throw new GlobeException(SystemConstants.FAIL_CODE, "获取设备型号为空!");
        }
        if (userId == null || userId == 0) {
            throw new GlobeException(SystemConstants.FAIL_CODE, "请输入正确的玩家账号!");
        }
        AccReportVO accReportVO = new AccReportVO();
        accReportVO.setUserId(userId);
        accReportVO.setIp(ip);
        accReportVO.setPhoneType(phoneType);
        accReportVO.setLoginArea(loginArea);
        Boolean flag = accountsClient.updateLoginIp(accReportVO);
        GlobeResponse<Object> globeResponse = new GlobeResponse<>();
        globeResponse.setData(flag);
        return globeResponse;
    }

    /**
     * 获取业主热更版本号
     */
    @RequestMapping("/getHotVersion")
    public GlobeResponse<Object> getHotVersion(Integer agentId) {
        if (null == agentId || agentId == 0) {
            throw new GlobeException(SystemConstants.FAIL_CODE, "参数错误!");
        }
        String hotVersion = agenteClient.getAgentHotVersion(agentId);
        GlobeResponse<Object> globeResponse = new GlobeResponse<>();
        globeResponse.setData(hotVersion);
        return globeResponse;
    }


    /**
     * 获取用户余额宝收益
     */
    @RequestMapping("/getyebProfitDetailsList")
    public GlobeResponse<Object> getyebProfitDetailsList(Integer userId, Integer pageIndex, Integer pageSize) {
        if (userId == null || userId == 0) {
            throw new GlobeException(SystemConstants.FAIL_CODE, "参数错误");
        }
        List<yebProfitDetailsVO> lists = treasureServiceClient.getyebProfitDetailsList(userId, pageIndex, pageSize);
        GlobeResponse<Object> globeResponse = new GlobeResponse<>();
        globeResponse.setData(lists);
        return globeResponse;
    }

    /**
     * 余额宝总收入
     */
    @RequestMapping("/getYwbAfterInfo")
    public GlobeResponse<Object> getYwbAfterInfo(Integer userId) {
        if (userId == null || userId == 0) {
            throw new GlobeException(SystemConstants.FAIL_CODE, "参数错误");
        }
        yebProfitDetailsVO lists = treasureServiceClient.getYwbAfterInfo(userId);
        if (lists == null) {
            lists =new yebProfitDetailsVO();
            lists.setYesterdayAfter(BigDecimal.ZERO);
            lists.setTotalAfter(BigDecimal.ZERO);
            lists.setInsureScore(BigDecimal.ZERO);
        }
        GlobeResponse<Object> globeResponse = new GlobeResponse<>();
        globeResponse.setData(lists);
        return globeResponse;
    }
    
    /**
     * 今日代理排行
     */
    @RequestMapping("/getTodayRankingList")
    public GlobeResponse<Object> getTodayRankingList(Integer userId, Integer parentId) {
        if (parentId == null || parentId == 0) {
            throw new GlobeException(SystemConstants.FAIL_CODE, "参数错误");
        }        
        GlobeResponse<Object> globeResponse = new GlobeResponse<>();
        List<DayUserAbsScoreVO> list = treasureServiceClient.getTodayRankingList(parentId);
        if(list == null || list.size() == 0) {
        	return globeResponse;
        }
        RankingListVO data = new RankingListVO();
        List<Ranking> rankingList = new ArrayList<>();
        data.setRankingList(rankingList);
        for(DayUserAbsScoreVO l : list) {
        	Ranking r = new Ranking();
        	r.setGameId(l.getGameId());
        	r.setNickName(l.getNickName());
        	r.setRanking(l.getRanking());
        	r.setScore(l.getScore());
        	if(l.getUserId().equals(userId)) {
        		data.setMyRanking(l.getRanking());
        		data.setScore(l.getScore());
        	}
        	rankingList.add(r);
        }
        if(data.getScore() == BigDecimal.ZERO) {
        	DayUserAbsScoreVO me = treasureServiceClient.getMyTodayRanking(userId);
        	if(null != me) {
        		data.setScore(me.getScore());
        	}
        }
        globeResponse.setData(data);
        return globeResponse;
    }
    
    /**
     * 昨日代理排行
     */
    @RequestMapping("/getTomorrowRankingList")
    public GlobeResponse<Object> getTomorrowRankingList(Integer userId, Integer parentId) {
        if (parentId == null || parentId == 0) {
            throw new GlobeException(SystemConstants.FAIL_CODE, "参数错误");
        }        
        GlobeResponse<Object> globeResponse = new GlobeResponse<>();
        List<DayRankingRewardVO> list = agenteClient.getTomorrowRankingList(parentId);
        if(list == null || list.size() == 0) {
        	return globeResponse;
        }
        RankingListVO data = new RankingListVO();
        List<Ranking> rankingList = new ArrayList<>();
        data.setRankingList(rankingList);
        for(DayRankingRewardVO l : list) {
        	Ranking r = new Ranking();
        	r.setGameId(l.getGameId());
        	r.setNickName(l.getNickName());
        	r.setRanking(l.getRanking());
        	r.setScore(l.getScore());
        	r.setReward(l.getRewardScore());
        	if(l.getUserId().equals(userId)) {
        		data.setMyRanking(l.getRanking());
        		data.setMyReward(l.getRewardScore());
        		data.setId(l.getId());
        		data.setScore(l.getScore());
        		data.setRewardStatus(l.getRewardStatus());
        	}
        	rankingList.add(r);
        }
        if(data.getScore() == BigDecimal.ZERO) {
        	DayRankingRewardVO me = agenteClient.getMyTomorrowRanking(userId);
        	if(null != me) {
        		data.setScore(me.getScore());
        	}
        }
        globeResponse.setData(data);
        return globeResponse;
    }
    
    /**
     * 领取奖励
     */
    @RequestMapping("/receiveReward")
    public GlobeResponse<Object> receiveReward(Integer id) {
        if (id == null || id == 0) {
            throw new GlobeException(SystemConstants.FAIL_CODE, "参数错误");
        }        
        GlobeResponse<Object> globeResponse = new GlobeResponse<>();
        Map<String, Object> param = this.agenteClient.receiveReward(id);
        int ret = (Integer) param.get("ret");
        if(ret == 0)
        	return globeResponse;
        throw new GlobeException(SystemConstants.FAIL_CODE, param.get("msg").toString());
    }
}
