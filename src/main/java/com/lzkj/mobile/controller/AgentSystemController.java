package com.lzkj.mobile.controller;

import com.lzkj.mobile.client.*;
import com.lzkj.mobile.config.AgentSystemEnum;
import com.lzkj.mobile.config.SystemConstants;
import com.lzkj.mobile.exception.GlobeException;
import com.lzkj.mobile.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/agentSystem")
@Slf4j
public class AgentSystemController {

    @Autowired
    private AgentServiceClient agentClient;

    @Autowired
    private AccountsServiceClient accountsClient;

    @Autowired
    private TreasureServiceClient treasureServiceClient;

    @Autowired
    private PlatformServiceClient platformServiceClient;

    @Autowired
    private NativeWebServiceClient nativeWebServiceClient;


    @Value("${channelGameUrl}")
    private String channelGameUrl;
    
    @Value("${gameImg.url}")
    private String gameImgUrl;

//    /**
//     * 全民代理 -我的推广(首页信息)
//     *
//     * @param userId
//     * @return
//     */
//    @RequestMapping("/getAgentMyPopularize")
//    private GlobeResponse<Object> getAgentMyPopularize(Integer userId) {
//        MyPopularizeVO agentSystemVO = agentClient.getAgentMyPopularize(userId);
//        GlobeResponse<Object> globeResponse = new GlobeResponse<>();
//        globeResponse.setData(agentSystemVO);
//        return globeResponse;
//    }

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
        List<MyPlayerVO> list = agentClient.getAgentMyPlayer(userId, memberId, pageIndex);
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

        List<MyPlayerVO> list = agentClient.getAgentMyTeam(userId, agentId, pageSize, pageIndex);
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
        List<QmAchievementVO> list = agentClient.getAchievement(userId);
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
        List<MyRewardRecordVO> list = agentClient.getMyRewardRecord(userId);
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
        List<MyQmTxRecord> list = agentClient.getMyQmTxRecord(userId, pageSize, pageIndex);
        MyRewardVO data = agentClient.getMyReward(userId);
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
    private GlobeResponse<Object> getZzSysRatio(Integer agentId,Integer userId) {
        if (agentId==null||agentId ==0){
            throw new GlobeException(SystemConstants.FAIL_CODE, "参数错误!");
        }
        List<ZzSysRatioVO> list = agentClient.getZzSysRatio(agentId);
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
        AgentAccVO agentAccVO = agentClient.getQrCode(agentId);
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
        log.info("agentId:"+agentId+"\t registerMachine:"+registerMachine);
        AgentAccVO agentAccVO = agentClient.getQrCode(agentId);
        String key = "EnjoinLogon";
        //总控的维护
        SystemStatusInfoVO systemStatusInfo = accountsClient.getSystemStatusInfo(key);
        Boolean flag = false;
        if (systemStatusInfo.getStatusValue().compareTo(BigDecimal.ZERO) != 0) {
            flag = true;
        }
        Map<String, Object> data = new HashMap<>();

        //获取业主配置
        List<AgentSystemStatusInfoVO> agentSystemList = agentClient.getBindMobileSendInfo(agentId);

        //获取首页弹窗链接

        String imgUrl = nativeWebServiceClient.getShowImgUrl(agentId);
        data.put("QRcode", agentAccVO.getAgentUrl());
        data.put("VERSION_APK", agentAccVO.getAgentVersion());
        data.put("ClientUrl", agentAccVO.getClientUrl());
        data.put("prompt", agentAccVO.getPrompt());
        data.put("channelGameUrl",channelGameUrl);
        data.put("showbanner",imgUrl);
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
                    	flag = true;
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
            //邮件系统是否开启
            if(vo.getStatusName().equals(AgentSystemEnum.MailOpen.getName())){
                if (vo.getStatusValue().compareTo(BigDecimal.ZERO) == 0) {
                    data.put("mail", true);
                } else {
                    data.put("mail", false);
                }
            }
            //签到
            if(vo.getStatusName().equals(AgentSystemEnum.SignOpen.getName())){
                if (vo.getStatusValue().compareTo(BigDecimal.ZERO) == 0) {
                    data.put("signUp", true);
                } else {
                    data.put("signUp", false);
                }
            }
            //代理排行榜
            if(vo.getStatusName().equals(AgentSystemEnum.AgentRank.getName())){
                if (vo.getStatusValue().compareTo(BigDecimal.ZERO) == 0) {
                    data.put("AgentRank", true);
                } else {
                    data.put("AgentRank", false);
                }
            }
            //修改密码开关
            if(vo.getStatusName().equals(AgentSystemEnum.ResetPwd.getName())){
                if (vo.getStatusValue().compareTo(BigDecimal.ZERO) == 0) {
                    data.put("canResetdhmm", true);
                } else {
                    data.put("canResetdhmm", false);
                }
            }
            //活动展示
            if(vo.getStatusName().equals(AgentSystemEnum.ActivityOpen.getName())) {
            	if(vo.getStatusValue().compareTo(BigDecimal.ZERO) == 0) {
            		 data.put("ActivityOpen", true);
                } else {
                    data.put("ActivityOpen", false);
                }
            }
           //提现展示
            if(vo.getStatusName().equals(AgentSystemEnum.ApplyOrderOpen.getName())) {
            	if(vo.getStatusValue().compareTo(BigDecimal.ZERO) == 0) {
            		 data.put("ApplyOrderOpen", true);
                } else {
                    data.put("ApplyOrderOpen", false);
                }
            }
            //余额宝是否开启
            if(vo.getStatusName().equals(AgentSystemEnum.YebOpen.getName())) {
            	if(vo.getStatusValue().compareTo(BigDecimal.ZERO) == 0) {
            		data.put("yebiIsopen", true);
            	}else {
            		data.put("yebiIsopen", false);
            	}
            }
        }
//        Integer typeId = 1;
	    List<PlatformVO> platfromList = platformServiceClient.getAgentGameListByGameTypeItem(Integer.valueOf(agentId));
        List<AgentMobileKindConfigVO> thirdList =  platformServiceClient.getAgentGameByGameTypeItem(Integer.valueOf(agentId));
        data.put("platfromList",platfromList);
        data.put("ThirdGameList",thirdList);
        data.put("imgUrl", gameImgUrl);
        List<CloudShieldConfigurationVO> vo = agentClient.getCloudShieldConfigurationInfos(agentId);
        if(vo != null) {
        	data.put("CloudData", vo);
        }
        LuckyTurntableConfigurationVO luckyTurntableConfigurationVO = treasureServiceClient.getLuckyIsOpen(agentId);
        if(luckyTurntableConfigurationVO !=null) {
        	data.put("luckyWheel", luckyTurntableConfigurationVO.getMainSwitch());
        }
        //判断预更新热更开关開啓沒
        if ("0".equals(String.valueOf(agentAccVO.getStatus()))) {
            String[] update = agentAccVO.getUpdateAddress().split(",");
            data.put("HOT_UPDATE_URL", update);
        }
//        else {
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
//        }
        data.put("Maitance", flag);
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
        String hotVersion = agentClient.getAgentHotVersion(agentId);
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
     * 本周代理排行
     */
    @RequestMapping("/getTodayRankingList")
    public GlobeResponse<Object> getTodayRankingList(Integer parentId) {
        if (parentId == null || parentId == 0) {
            throw new GlobeException(SystemConstants.FAIL_CODE, "参数错误");
        }
        GlobeResponse<Object> globeResponse = new GlobeResponse<>();
        List<DayUserAbsScoreVO> list = treasureServiceClient.getWeekRankList(parentId);
        if(list == null || list.size() == 0) {
        	return globeResponse;
        }
        globeResponse.setData(list);
        return globeResponse;
    }

    /**
     * 上周代理排行
     */
    @RequestMapping("/getTomorrowRankingList")
    public GlobeResponse<Object> getLastRankList(Integer parentId) {
        if (parentId == null || parentId == 0) {
            throw new GlobeException(SystemConstants.FAIL_CODE, "参数错误");
        }
        GlobeResponse<Object> globeResponse = new GlobeResponse<>();

       List<WeekRankingListVO> list = agentClient.getLastRankingList(parentId);
        //上周没有赠送的情况下，获取旧打码量数据
//        if(list == null || list.size() == 0) {
//            List<DayUserAbsScoreVO> lastWeekList = treasureServiceClient.getLastWeekRank(parentId);
//            globeResponse.setData(lastWeekList);
//            return globeResponse;
//        }
        globeResponse.setData(list);
        return globeResponse;
    }

    @RequestMapping("/getPlayerRankInfo")
    public GlobeResponse<Object> getPlayerRankInfo(Integer userId) {
        if (userId == null || userId == 0) {
            throw new GlobeException(SystemConstants.FAIL_CODE, "参数错误");
        }
        GlobeResponse<Object> globeResponse = new GlobeResponse<>();
        List<WeekRankingListVO> list = agentClient.getPlayerRankInfo(userId);
        if(list == null || list.size() == 0) {
            return globeResponse;
        }
        globeResponse.setData(list);
        return globeResponse;
    }
    /**
     * 代理排行榜奖励说明
     */
    @RequestMapping("/getAgentRankConfig")
    public GlobeResponse<Object> getAgentRankConfig(Integer parentId) {
        if (parentId == null || parentId == 0) {
            throw new GlobeException(SystemConstants.FAIL_CODE, "参数错误");
        }
        GlobeResponse<Object> globeResponse = new GlobeResponse<>();
        List<AgencyEqualReward> agentConfig = agentClient.getAgentRankList(parentId);
        globeResponse.setData(agentConfig);
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
        Map<String, Object> param = this.agentClient.receiveReward(id);
        int ret = (Integer) param.get("ret");
        if(ret == 0)
        	return globeResponse;
        throw new GlobeException(SystemConstants.FAIL_CODE, param.get("msg").toString());
    }

    /**
     * 提现流水详情
     */
    @RequestMapping("/cashFlowDetails")
    public GlobeResponse<Object> cashFlowDetails(Integer agentId,Integer gameId) {
        if (agentId == null || gameId == null) {
            throw new GlobeException(SystemConstants.FAIL_CODE, "参数错误");
        }
        GlobeResponse<Object> globeResponse = new GlobeResponse<>();
        //List<UserBetInfoVO>  vo =  accountsClient.getUserBetInfo(userId,agentId);
        List<Map<String, Object>> param = this.agentClient.cashFlowDetails(agentId,gameId);
        globeResponse.setData(param);
        return globeResponse;
    }

    /**
     * 资金明细
     */
    @RequestMapping("/fundDetails")
    public GlobeResponse<Object> fundDetails(Integer gameId,Integer agentId ) {
        if (gameId == null) {
            throw new GlobeException(SystemConstants.FAIL_CODE, "参数错误");
        }
        GlobeResponse<Object> globeResponse = new GlobeResponse<>();
        List<Map<String, Object>> param = this.agentClient.fundDetails(gameId,agentId);
        globeResponse.setData(param);
        return globeResponse;
    }
}
