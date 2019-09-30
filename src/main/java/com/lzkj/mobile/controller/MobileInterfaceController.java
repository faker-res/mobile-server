package com.lzkj.mobile.controller;

import static com.lzkj.mobile.config.AwardOrderStatus.getDescribe;
import static com.lzkj.mobile.util.HttpUtil.post;
import static com.lzkj.mobile.util.IpAddress.getIpAddress;
import static com.lzkj.mobile.util.MD5Utils.MD5Encode;
import static com.lzkj.mobile.util.MD5Utils.getAllFields;
import static com.lzkj.mobile.util.PayUtil.GetOrderIDByPrefix;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.lzkj.mobile.client.AccountsServiceClient;
import com.lzkj.mobile.client.AgentServiceClient;
import com.lzkj.mobile.client.NativeWebServiceClient;
import com.lzkj.mobile.client.PlatformServiceClient;
import com.lzkj.mobile.client.TreasureServiceClient;
import com.lzkj.mobile.config.AgentSystemEnum;
import com.lzkj.mobile.config.SiteConfigKey;
import com.lzkj.mobile.config.SystemConfigKey;
import com.lzkj.mobile.config.SystemConstants;
import com.lzkj.mobile.exception.GlobeException;
import com.lzkj.mobile.exception.YunpianException;
import com.lzkj.mobile.mongo.GameRecord;
import com.lzkj.mobile.redis.RedisDao;
import com.lzkj.mobile.redis.RedisKeyPrefix;
import com.lzkj.mobile.service.CheckUserSignatureService;
import com.lzkj.mobile.util.HttpRequest;
import com.lzkj.mobile.util.MD5Utils;
import com.lzkj.mobile.util.StringUtil;
import com.lzkj.mobile.util.TimeUtil;
import com.lzkj.mobile.vo.AccountsInfoVO;
import com.lzkj.mobile.vo.AgentAccVO;
import com.lzkj.mobile.vo.AgentInfoVO;
import com.lzkj.mobile.vo.AgentIsIosVO;
import com.lzkj.mobile.vo.ApplyRecordPageVo;
import com.lzkj.mobile.vo.AwardOrderPageVo;
import com.lzkj.mobile.vo.BankCardTypeVO;
import com.lzkj.mobile.vo.BindPhoneVO;
import com.lzkj.mobile.vo.ConfigInfo;
import com.lzkj.mobile.vo.CustomerServiceConfigVO;
import com.lzkj.mobile.vo.GameFeedbackVO;
import com.lzkj.mobile.vo.GameListVO;
import com.lzkj.mobile.vo.GamePropertyType;
import com.lzkj.mobile.vo.GatewayInfo;
import com.lzkj.mobile.vo.GetBankRecordVO;
import com.lzkj.mobile.vo.GlobalSpreadInfo;
import com.lzkj.mobile.vo.GlobeResponse;
import com.lzkj.mobile.vo.GoldExchangeVO;
import com.lzkj.mobile.vo.LotteryConfigVO;
import com.lzkj.mobile.vo.LuckyTurntableConfigurationVO;
import com.lzkj.mobile.vo.MobileAwardOrderVo;
import com.lzkj.mobile.vo.MobileDayTask;
import com.lzkj.mobile.vo.MobileKind;
import com.lzkj.mobile.vo.MobileNoticeVo;
import com.lzkj.mobile.vo.MobilePropertyTypeVO;
import com.lzkj.mobile.vo.MobileShareConfigVO;
import com.lzkj.mobile.vo.NewsVO;
import com.lzkj.mobile.vo.OnLineOrderVO;
import com.lzkj.mobile.vo.PayInfoVO;
import com.lzkj.mobile.vo.PayTypeList;
import com.lzkj.mobile.vo.ProblemConfigVO;
import com.lzkj.mobile.vo.RecordInsurePageVO;
import com.lzkj.mobile.vo.RecordInsureVO;
import com.lzkj.mobile.vo.ScoreRankVO;
import com.lzkj.mobile.vo.ShareDetailInfoVO;
import com.lzkj.mobile.vo.SystemStatusInfoVO;
import com.lzkj.mobile.vo.TpayOwnerInfoVO;
import com.lzkj.mobile.vo.UserGameScoreInfoVO;
import com.lzkj.mobile.vo.UserScoreRankVO;
import com.lzkj.mobile.vo.VerificationCodeVO;
import com.lzkj.mobile.vo.ViewPayInfoVO;
import com.lzkj.mobile.vo.VisitorBindResultVO;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@RestController
@RequestMapping("/mobileInterface")
public class MobileInterfaceController {

    @Value("${phonePostUrl}")
    private String phonePostUrl;

    @Value("${phoneName}")
    private String phoneName;

    @Value("${phonePwd}")
    private String phonePwd;

    @Value("${mobileInterfaceUrl}")
    private String mobileInterfaceUrl;

    @Value("${server.url}")
    private String serverUrl;

//    @Value("#{${pay.url}}")
//    private Map<String, String> payUrlList;


    @Autowired
    private TreasureServiceClient treasureServiceClient;

    @Autowired
    private AccountsServiceClient accountsServiceClient;

    @Autowired
    private NativeWebServiceClient nativeWebServiceClient;

    @Autowired
    private PlatformServiceClient platformServiceClient;

    @Autowired
    private AgentServiceClient agentServiceClient;

    @Autowired
    private RedisDao redisDao;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    @Lazy
    private CheckUserSignatureService checkUserSignatureService;

    @RequestMapping("/getScoreRank")
    private GlobeResponse<List<UserScoreRankVO>> getScoreRank(HttpServletRequest request) {
        String pageIndexParam = request.getParameter("pageIndex");
        String pageSizeParam = request.getParameter("pageSize");
        String userIdParam = request.getParameter("userId");
        int pageIndex = pageIndexParam == null ? 1 : Integer.parseInt(pageIndexParam);
        int pageSize = pageSizeParam == null ? 10 : Integer.parseInt(pageSizeParam);
        Integer userId = userIdParam == null ? 0 : Integer.parseInt(userIdParam);
        if (pageSize > 50) {
            pageSize = 50;
        }
        List<UserGameScoreInfoVO> userGameScoreInfoList = treasureServiceClient.getUserGameScoreInfoList(userId);
        int chartId = 0;
        BigDecimal score = BigDecimal.ZERO;
        if (userGameScoreInfoList.size() > 0) {
            UserGameScoreInfoVO first = userGameScoreInfoList.get(0);
            chartId = first.getChartId();
            score = first.getScore();
        }
        List<ScoreRankVO> scoreRankList = treasureServiceClient.getScoreRankList(pageIndex, pageSize).getScoreRankList();
        List<UserScoreRankVO> data = new ArrayList<>();
        if (scoreRankList != null && scoreRankList.size() > 0) {
            AccountsInfoVO accountsInfo = accountsServiceClient.getAccountsInfo(userId);
            String nickname = "";
            if (accountsInfo != null && accountsInfo.getNickname() != null) {
                nickname = accountsInfo.getNickname();
            }
            UserScoreRankVO r = new UserScoreRankVO();
            r.setScore(score);
            r.setNickName(nickname);
            r.setRank(chartId);
            data.add(r);
            for (ScoreRankVO item : scoreRankList) {
                accountsInfo = accountsServiceClient.getAccountsInfo(item.getUserId());
                nickname = "";
                if (accountsInfo != null && accountsInfo.getNickname() != null) {
                    nickname = accountsInfo.getNickname();
                }
                r = new UserScoreRankVO();
                r.setScore(item.getScore());
                r.setNickName(nickname);
                data.add(r);
            }
        }
        GlobeResponse<List<UserScoreRankVO>> globeResponse = new GlobeResponse<>();
        globeResponse.setData(data);
        return globeResponse;
    }

    /**
     * 获取游戏列表
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/getGameList")
    private GlobeResponse<GameListVO> getGameList(HttpServletRequest request, HttpServletResponse response) {
        String typeIdParam = request.getParameter("typeId");

        int typeId = typeIdParam == null ? 1 : Integer.parseInt(typeIdParam);
        if (typeId != 1 && typeId != 2) {
            typeId = 1;
        }
        String key = request.getParameter("key");
        SiteConfigKey siteConfigKey = SiteConfigKey.MobilePlatformVersion;
        if (key != null) {
            if (key.equals("android")) {
                siteConfigKey = SiteConfigKey.GameAndroidConfig;
            } else if (key.equals("ios")) {
                siteConfigKey = SiteConfigKey.GameIosConfig;
            } else if (key.equals("win32")) {
                siteConfigKey = SiteConfigKey.GameWin32Config;
            }
        }

        ConfigInfo configInfo = nativeWebServiceClient.getConfigInfo(siteConfigKey.toString());
        String value = "";
        String value2 = "";
        String value3 = "";
        String value4 = "";
        String value5 = "";
        String value6 = "";
        String value7 = "";
        String value8 = "";
        if (configInfo != null) {
            value2 = configInfo.getField1();
            value = configInfo.getField2();
            value3 = configInfo.getField3();
            value4 = configInfo.getField4();
            value5 = configInfo.getField5();
            value6 = configInfo.getField6();
            value7 = configInfo.getField7();
        }
        SystemConfigKey systemConfigKey = SystemConfigKey.IsOpenRoomCard;
        SystemStatusInfoVO systemStatusInfo = accountsServiceClient.getSystemStatusInfo(systemConfigKey.toString());
        int status2 = 0;
        systemConfigKey = SystemConfigKey.WxLogon;
        SystemStatusInfoVO systemStatusInfo2 = accountsServiceClient.getSystemStatusInfo(systemConfigKey.toString());
        if (systemStatusInfo2 != null) {
            status2 = systemStatusInfo2.getStatusValue().intValue();
        }
        String agentId = request.getParameter("agentId");
        List<MobileKind> mobileKindList = platformServiceClient.getMobileKindList(typeId, Integer.valueOf(agentId));
        GameListVO data = new GameListVO();
        data.setValid(true);
        data.setDownloadUrl(value2);
        data.setDebugUrl(value5);
        data.setWxLogon(status2);
        data.setIsOpenCard(systemStatusInfo == null ? 1 : systemStatusInfo.getStatusValue().intValue());
        data.setGameList(mobileKindList);
        data.setPackageName(value6);
        int isIosShop = 0;
        if (agentId == null) {
            String agentAcc = request.getParameter("agentAcc");
            if (agentAcc == null || agentAcc.equals("")) {
                isIosShop = Integer.valueOf(value7);
            } else {
                List<AgentIsIosVO> agentIsIosList = agentServiceClient.getAppStore(agentAcc);
                if (agentIsIosList != null && agentIsIosList.size() > 0) {
                    AgentIsIosVO firstRow = agentIsIosList.get(0);
                    value = String.valueOf(firstRow.getVersionNo());
                    value3 = String.valueOf(firstRow.getSrcVersionNo());
                    value4 = String.valueOf(firstRow.getAppUrl());
                }
            }
        } else {
            List<AgentIsIosVO> agentIsIosList = agentServiceClient.getAppStoreById(Integer.valueOf(agentId));
            if (agentIsIosList != null && agentIsIosList.size() > 0) {
                AgentIsIosVO firstRow = agentIsIosList.get(0);
                isIosShop = firstRow.getIsIOSShop();
                value = String.valueOf(firstRow.getVersionNo());
                value3 = String.valueOf(firstRow.getSrcVersionNo());
            }
        }
        data.setIosUrl(value4);
        data.setClientVersion(value);
        data.setResVersion(value3);
        data.setAppStore(isIosShop);
        data.setIosVersion(value8);
        GlobeResponse<GameListVO> globeResponse = new GlobeResponse<>();
        globeResponse.setData(data);
        return globeResponse;
    }

    @RequestMapping("/getMobileProperty")
    private GlobeResponse<MobilePropertyTypeVO> getMobileProperty(HttpServletRequest request) {
        String typeIdParam = request.getParameter("typeId");
        Integer typeId = typeIdParam == null ? 0 : Integer.valueOf(typeIdParam);
        List<GamePropertyType> mobilePropertyTypeList = platformServiceClient.getMobilePropertyType(typeId);
        MobilePropertyTypeVO data = new MobilePropertyTypeVO();
        data.setValid(true);
        data.setList(mobilePropertyTypeList);
        GlobeResponse<MobilePropertyTypeVO> globeResponse = new GlobeResponse<>();
        globeResponse.setData(data);
        return globeResponse;
    }

    @RequestMapping("/getBankRecord")
    private GlobeResponse<Object> getBankRecord(HttpServletRequest request) throws Exception {
        String userIdParam = request.getParameter("userId");
        String pageIndexParam = request.getParameter("pageIndex");
        String pageSizeParam = request.getParameter("pageSize");
        //判空
        Integer pageIndex = pageIndexParam == null ? 1 : Integer.parseInt(pageIndexParam);
        Integer pageSize = pageSizeParam == null ? 20 : Integer.parseInt(pageSizeParam);
        Integer userId = userIdParam == null ? 0 : Integer.parseInt(userIdParam);
        checkUserSignatureService.checkUserSignature(userId);
        RecordInsurePageVO pageVo = treasureServiceClient.getInsureTradeRecord(pageIndex, pageSize, userId);
        Map<String, Object> data = new HashMap<>(8);
        if (pageVo != null && pageVo.getRecordCount() > 0) {
            List<RecordInsureVO> insureVoList = pageVo.getRecordList();
            List<GetBankRecordVO> list = new ArrayList<>();
            for (RecordInsureVO l : insureVoList) {
                GetBankRecordVO getBankRecordResponse = new GetBankRecordVO();
                getAllFields(l.getClass());
                getBankRecordResponse.setTradeType(l.getTradeType());
                getBankRecordResponse.setSwapScore(l.getSwapScore());
                getBankRecordResponse.setClientIp(l.getClientIp());
                getBankRecordResponse.setCollectDate(TimeUtil.format(l.getCollectDate()));
                getBankRecordResponse.setCollectNote(l.getCollectNote());
                getBankRecordResponse.setIsGamePlaza(l.getIsGamePlaza());
                getBankRecordResponse.setKindId(l.getKindId());
                getBankRecordResponse.setRecordId(l.getRecordId());
                getBankRecordResponse.setRevenue(l.getRevenue());
                getBankRecordResponse.setServerId(l.getServerId());
                getBankRecordResponse.setSourceGold(l.getSourceGold());
                getBankRecordResponse.setSourceUserId(l.getSourceUserId());
                getBankRecordResponse.setTargetBank(l.getTargetBank());
                getBankRecordResponse.setSourceGold(l.getSourceGold());
                getBankRecordResponse.setTargetGold(l.getTargetGold());
                getBankRecordResponse.setTargetUserId(l.getTargetUserId());
                if (l.getTradeType() == 1) {
                    getBankRecordResponse.setTradeTypeDescription("银行存款");
                    getBankRecordResponse.setTransferAccounts("");
                } else if (l.getTradeType() == 2) {
                    getBankRecordResponse.setTradeTypeDescription("银行取款");
                    getBankRecordResponse.setTransferAccounts("");
                } else {
                    if (l.getSourceUserId() == userId) {
                        BigDecimal swapScore = l.getSwapScore().negate();
                        l.setSwapScore(swapScore);
                        getBankRecordResponse.setTradeTypeDescription("银行转账");
                        AccountsInfoVO accountsInfo = accountsServiceClient.getAccountsInfo(l.getTargetUserId());
                        getBankRecordResponse.setTransferAccounts(String.valueOf(accountsInfo.getGameId()));
                        getBankRecordResponse.setSwapScore(l.getSwapScore());
                    } else {
                        getBankRecordResponse.setTradeTypeDescription("银行收款");
                        AccountsInfoVO accountsInfo = accountsServiceClient.getAccountsInfo(l.getSourceUserId());
                        getBankRecordResponse.setTransferAccounts(String.valueOf(accountsInfo.getGameId()));
                    }
                }
                list.add(getBankRecordResponse);
            }
            data.put("total", pageVo.getRecordCount());
            data.put("list", list);
        }
        GlobeResponse<Object> globeResponse = new GlobeResponse<>();
        globeResponse.setData(data);
        return globeResponse;
    }

    /**
     * 提现记录
     *
     * @param request
     * @return
     */
    @RequestMapping("/getWithdrawalRecord")
    private GlobeResponse<Object> getTiXianRecord(HttpServletRequest request) {
        String userIdParam = request.getParameter("userId");
        String pageIndexParam = request.getParameter("pageIndex");
        String pageSizeParam = request.getParameter("pageSize");
        //判空
        Integer pageIndex = pageIndexParam == null ? 1 : Integer.parseInt(pageIndexParam);
        Integer pageSize = pageSizeParam == null ? 20 : Integer.parseInt(pageSizeParam);
        Integer userId = userIdParam == null ? 0 : Integer.parseInt(userIdParam);
        // checkUserSignatureService.checkUserSignature(userId);
        ApplyRecordPageVo pageVo = accountsServiceClient.getApplyOrder(userId, pageIndex, pageSize);
        Map<String, Object> data = new HashMap<>(8);
        data.put("total", pageVo.getRecordCount());
        data.put("list", pageVo.getApplyOrderVo());
        GlobeResponse<Object> globeResponse = new GlobeResponse<>();
        globeResponse.setData(data);
        return globeResponse;
    }

    @RequestMapping("/getMobileRollNotice")
    private GlobeResponse<List<MobileNoticeVo>> getMobileRollNotice(HttpServletRequest request, Integer agentId) {
        if (agentId == null) {
            throw new GlobeException(SystemConstants.FAIL_CODE, "参数错误");
        }
        List<NewsVO> newsList = nativeWebServiceClient.getMobileNotice(agentId);
        List<MobileNoticeVo> mobileNoticeVoList = new ArrayList<>();
        newsList.stream().forEach(l -> {
            MobileNoticeVo mobileNoticeVo = new MobileNoticeVo();
            mobileNoticeVo.setId(l.getNewsId());
            mobileNoticeVo.setTitle(l.getSubject());
            mobileNoticeVo.setDate(l.getIssueDate());
            mobileNoticeVo.setContent(l.getBody());
            mobileNoticeVoList.add(mobileNoticeVo);
        });
        GlobeResponse<List<MobileNoticeVo>> globeResponse = new GlobeResponse<>();
        globeResponse.setData(mobileNoticeVoList);
        return globeResponse;
    }

    @RequestMapping("/getServerConfig")
    private GlobeResponse<Object> getServerConfig(HttpServletRequest request) {
        SiteConfigKey serverConfigKey = SiteConfigKey.ServerConfig;
        ConfigInfo configInfo = nativeWebServiceClient.getConfigInfo(serverConfigKey.toString());
        String value = "";
        String value2 = "";
        String value3 = "";
        String value4 = "";
        String value5 = "";
        String value6 = "";
        String value7 = "";
        String value8 = "";
        if (configInfo != null) {
            value = configInfo.getField1();
            value2 = configInfo.getField2();
            value3 = configInfo.getField3();
            value4 = configInfo.getField4();
            value5 = configInfo.getField5();
            value6 = configInfo.getField6();
            value7 = configInfo.getField7();
        }
        Map<String, String> data = new HashMap<>();
        data.put("str1", value);
        data.put("str2", value2);
        data.put("str3", value3);
        data.put("str4", value4);
        data.put("str5", value5);
        data.put("str6", value6);
        data.put("str7", value7);
        data.put("str8", value8);
        GlobeResponse<Object> globeResponse = new GlobeResponse<>();
        globeResponse.setData(data);
        return globeResponse;
    }

    @RequestMapping("/getOrderRecord")
    private GlobeResponse<Object> getOrderRecord(HttpServletRequest request) {
        String userIdParam = request.getParameter("userId");
        String pageIndexParam = request.getParameter("pageIndex");
        String pageSizeParam = request.getParameter("pageSize");
        //判空
        Integer pageIndex = pageIndexParam == null ? 1 : Integer.parseInt(pageIndexParam);
        Integer pageSize = pageSizeParam == null ? 20 : Integer.parseInt(pageSizeParam);
        Integer userId = userIdParam == null ? 0 : Integer.parseInt(userIdParam);
        checkUserSignatureService.checkUserSignature(userId);
        AwardOrderPageVo pageVo = nativeWebServiceClient.getAwardOrder(pageIndex, pageSize, userId);
        Map<String, Object> data = new HashMap<>();
        data.put("total", 0);
        data.put("list", new ArrayList<>());
        List<MobileAwardOrderVo> list = new ArrayList<>();
        if (pageVo.getRecordCount() > 0) {
            pageVo.getApplyOrderVo().stream().forEach(l -> {
                MobileAwardOrderVo mobileAwardOrderVo = new MobileAwardOrderVo();
                getAllFields(l);
                mobileAwardOrderVo.setUserId(l.getUserId());
                mobileAwardOrderVo.setAwardId(l.getAwardId());
                mobileAwardOrderVo.setArea(l.getArea());
                mobileAwardOrderVo.setBuyDate(l.getBuyDate());
                mobileAwardOrderVo.setBuyIP(l.getBuyIP());
                mobileAwardOrderVo.setCity(l.getCity());
                mobileAwardOrderVo.setCompellation(l.getCompellation());
                mobileAwardOrderVo.setDwellingPlace(l.getDwellingPlace());
                mobileAwardOrderVo.setMobilePhone(l.getMobilePhone());
                mobileAwardOrderVo.setAwardCount(l.getAwardCount());
                mobileAwardOrderVo.setOrderStatus(l.getOrderStatus());
                mobileAwardOrderVo.setAwardPrice(l.getAwardPrice());
                mobileAwardOrderVo.setOrderId(l.getOrderId());
                mobileAwardOrderVo.setPostalCode(l.getPostalCode());
                mobileAwardOrderVo.setProvince(l.getProvince());
                mobileAwardOrderVo.setQQ(l.getQQ());
                mobileAwardOrderVo.setSolveDate(l.getSolveDate());
                mobileAwardOrderVo.setSolveNote("");
                mobileAwardOrderVo.setTotalAmount(l.getTotalAmount());
                mobileAwardOrderVo.setOrderStatusDescription(getDescribe(l.getOrderStatus()));
                list.add(mobileAwardOrderVo);
            });
            data.put("total", pageVo.getRecordCount());
            data.put("list", list);
        }
        GlobeResponse<Object> globeResponse = new GlobeResponse<>();
        globeResponse.setData(data);
        return globeResponse;
    }

    /**
     * 获取新闻列表
     *
     * @return
     */
    @RequestMapping("/adsNotice")
    private GlobeResponse<List<NewsVO>> getGameNotice(Integer agentId) {
        if (agentId == null) {
            throw new GlobeException(SystemConstants.FAIL_CODE, "参数错误");
        }
        GlobeResponse<List<NewsVO>> globeResponse = new GlobeResponse<>();
        globeResponse.setData(nativeWebServiceClient.getGameNotice(1, agentId));
        return globeResponse;
    }

    @RequestMapping("/getMobileShareConfig")
    private GlobeResponse<MobileShareConfigVO> getMobileShareConfig(HttpServletRequest request) {
        int systemStatus = 0;
        SystemConfigKey systemConfigKey = SystemConfigKey.SharePresent;
        SystemStatusInfoVO systemStatusInfo = accountsServiceClient.getSystemStatusInfo(systemConfigKey.toString());
        if (systemStatusInfo != null) {
            systemStatus = systemStatusInfo.getStatusValue().intValue();
        }
        int freeCount = 3;
        LotteryConfigVO lotteryConfig = treasureServiceClient.getLotteryConfig();
        if (lotteryConfig != null) {
            freeCount = lotteryConfig.getFreeCount();
        }
        SiteConfigKey siteConfigKey = SiteConfigKey.DayTaskConfig;
        ConfigInfo configInfo = nativeWebServiceClient.getConfigInfo(siteConfigKey.toString());
        MobileDayTask value = new MobileDayTask("0", "0", "0", "0", "0", "0");
        if (configInfo != null) {
            value.setFeld1(configInfo.getField1());
            value.setFeld2(configInfo.getField2());
            value.setFeld3(configInfo.getField3());
            value.setFeld4(configInfo.getField4());
            value.setFeld5(configInfo.getField5());
            value.setFeld6(configInfo.getField6());
        }
        int registerGrantScore = 1888;
        GlobalSpreadInfo globalSpreadInfo = treasureServiceClient.getGlobalSpreadInfo();
        if (globalSpreadInfo != null) {
            registerGrantScore = globalSpreadInfo.getRegisterGrantScore();
        }
        MobileShareConfigVO data = new MobileShareConfigVO();
        data.setSharePresent(systemStatus);
        data.setFreeCount(freeCount);
        data.setDayTask(value);
        data.setRegGold(registerGrantScore);

        String configRDKey = RedisKeyPrefix.getKey("configInfo");
        configInfo = redisDao.get(configRDKey, ConfigInfo.class);
        if (configInfo == null) {
            configInfo = nativeWebServiceClient.getConfigInfo("ShareConfig");
            if (configInfo != null) {
                redisDao.set(configRDKey, configInfo);
            }
        }

        String value2 = "";
        String value3 = "";
        String value4 = "";
        String value5 = "";
        if (configInfo != null) {
            value2 = configInfo.getField1();
            value3 = configInfo.getField2();
            value4 = configInfo.getField3();
            value5 = configInfo.getField7();
        }
        data.setShareUrl(value2);
        data.setShareTitle(value3);
        data.setShareContent(value4);
        data.setEarnShareContent(value5);
        data.setValid(true);
        GlobeResponse<MobileShareConfigVO> GlobeResponse = new GlobeResponse<>();
        GlobeResponse.setData(data);
        return GlobeResponse;
    }

    /**
     * 获取验证码
     *
     * @param phone
     * @param type
     * @return sendMode = 1 为完美短信发送   2 为3D短信发送    3-4 都为众鑫短信   5 为188短信   6 为天朝短信   7 为至尊短信
     * 8 联众短信        9 为金鼎国际短信   10 为广发短信   11 为金利来
     */
    @RequestMapping("/getCode")
    private GlobeResponse<Object> getCode(String phone, String type, Integer agentId) {
        if (phone == null || phone.length() < 11) {
            throw new GlobeException(SystemConstants.FAIL_CODE, "手机号格式错误");
        }
        if (agentId == null || agentId == 0) {
            throw new GlobeException(SystemConstants.FAIL_CODE, "代理参数错误");
        }

        //验证手机号码是否存在黑名单中
        Integer phoneNum = agentServiceClient.getPhoneCount(phone, agentId);
        if (phoneNum > 0) {
            throw new GlobeException(SystemConstants.FAIL_CODE, "您的手机号码无权限获取，请联系客服！");
        }
        if (type == null) {
            type = "Register";
        } else if (!type.equals("Register") && !type.equals("SetPass") && !type.equals("BindPhone") && !type.equals("ModifyBankInfo")) {
            throw new GlobeException(SystemConstants.FAIL_CODE, "参数错误");
        }
        if (type.equals("SetPass")) {
            //验证手机是否注册过
            Integer num = accountsServiceClient.queryRegisterMobile(phone);
            if (num == 0) {
                GlobeResponse<Object> globeResponse = new GlobeResponse<>();
                globeResponse.setCode(SystemConstants.FAIL_CODE);
                globeResponse.setMsg("该手机号尚未注册,请确认!");
                return globeResponse;
            }
        }
        String vCode = StringUtil.getVerificationCode(6);

        String resTxt = "";
        Integer sendMode = agentServiceClient.getPhoneAgent(agentId);
        if (sendMode == null || sendMode == 0) {
            resTxt = sendCode(phone, vCode);
            if (resTxt.indexOf("<code>2</code>") > -1) {
                GlobeResponse<Object> globeResponse = new GlobeResponse<>();
                String key = RedisKeyPrefix.getKey(phone + ":" + type);
                VerificationCodeVO verificationCode = new VerificationCodeVO();
                verificationCode.setCode(vCode);
                redisDao.set(key, verificationCode);
                redisDao.expire(key, 11, TimeUnit.MINUTES);
                return globeResponse;
            }
            log.info("短信发送失败：" + resTxt);
            if (resTxt.indexOf("<code>4085</code>") > -1) {
                GlobeResponse<Object> globeResponse = new GlobeResponse<>();
                globeResponse.setCode(SystemConstants.FAIL_CODE);
                globeResponse.setMsg("一个手机号一天只能发送5条短信验证码");
                return globeResponse;
            }
        }
        //完美、3D短信
        if (sendMode == 1 || sendMode == 2) {
            String jsonTxt = null;
            try {
                jsonTxt = singleSend(phone, vCode, sendMode);
            } catch (YunpianException e) {
                //log.info("YunpianException:{}", e);
                String eMessage = e.getMessage();
                JSONObject jsonObject = JSONObject.parseObject(eMessage);
                if (jsonObject.getString("http_status_code").equals("400")) {
                    GlobeResponse<Object> globeResponse = new GlobeResponse<>();
                    globeResponse.setCode(SystemConstants.FAIL_CODE);
                    globeResponse.setMsg(jsonObject.getString("detail"));
                    return globeResponse;
                }
            }
            JSONObject jsStr = JSONObject.parseObject(jsonTxt);
            //log.info("-----------sendMode:" + sendMode + ",jsStr:" +jsStr);
            if (jsStr.getInteger("code").equals(0)) {
                GlobeResponse<Object> globeResponse = new GlobeResponse<>();
                String key = RedisKeyPrefix.getKey(phone + ":" + type);
                VerificationCodeVO verificationCode = new VerificationCodeVO();
                verificationCode.setCode(vCode);
                //  log.info("----key:" + key);
                redisDao.set(key, verificationCode);
                redisDao.expire(key, 11, TimeUnit.MINUTES);
                //log.info("----取出来:" + JsonUtil.parseJsonString(redisDao.get(key, VerificationCodeVO.class)));
                return globeResponse;
            }
            if (jsStr.getString("http_status_code").equals("400") || Integer.valueOf(jsStr.getString("code")) < 0) {
                GlobeResponse<Object> globeResponse = new GlobeResponse<>();
                globeResponse.setCode(SystemConstants.FAIL_CODE);
                globeResponse.setMsg(jsStr.getString("detail"));
                return globeResponse;
            }
            log.info("短信发送失败：" + resTxt);
        }

        if (sendMode == 3 || sendMode == 5 || sendMode == 6 || sendMode == 7 || sendMode == 9 || sendMode == 10 || sendMode == 11) {
            String jsonTxt = singleUSend(phone, vCode, sendMode);
            JSONObject jsStr = JSONObject.parseObject(jsonTxt);
            String result = jsStr.getString("data");
            JSONArray jsonObject = JSONObject.parseArray(result);
            log.info("接收第三参数：" + jsonObject);
            if (jsStr.getInteger("total_fee") > 10) {
                GlobeResponse<Object> globeResponse = new GlobeResponse<>();
                globeResponse.setCode(SystemConstants.FAIL_CODE);
                globeResponse.setMsg("一个手机号一天只能发送10条短信验证码");
                return globeResponse;
            }
            if (((JSONObject) jsonObject.get(0)).getInteger("code").equals(0)) {
                GlobeResponse<Object> globeResponse = new GlobeResponse<>();
                String key = RedisKeyPrefix.getKey(phone + ":" + type);
                VerificationCodeVO verificationCode = new VerificationCodeVO();
                verificationCode.setCode(vCode);
                redisDao.set(key, verificationCode);
                redisDao.expire(key, 11, TimeUnit.MINUTES);
                return globeResponse;
            }
            if (!((JSONObject) jsonObject.get(0)).getInteger("code").equals(0)) {
                GlobeResponse<Object> globeResponse = new GlobeResponse<>();
                globeResponse.setCode(SystemConstants.FAIL_CODE);
                globeResponse.setMsg(((JSONObject) jsonObject.get(0)).getString("msg"));
                return globeResponse;
            }
            log.info("短信发送失败：" + resTxt);
        }

        if (sendMode == 4 || sendMode == 8) {
            try {
                SendSmsResponse response = singleALYSend(phone, vCode, sendMode);
                log.info("阿里云短信接口返回的数据----------------");
                log.info("Code=" + response.getCode());
                log.info("Message=" + response.getMessage());
                log.info("RequestId=" + response.getRequestId());
                log.info("BizId=" + response.getBizId());
                if (response.getCode().equals("OK")) {
                    GlobeResponse<Object> globeResponse = new GlobeResponse<>();
                    String key = RedisKeyPrefix.getKey(phone + ":" + type);
                    VerificationCodeVO verificationCode = new VerificationCodeVO();
                    verificationCode.setCode(vCode);
                    redisDao.set(key, verificationCode);
                    redisDao.expire(key, 11, TimeUnit.MINUTES);
                    return globeResponse;
                }
                if (!response.getCode().equals("OK")) {
                    GlobeResponse<Object> globeResponse = new GlobeResponse<>();
                    globeResponse.setCode(SystemConstants.FAIL_CODE);
                    globeResponse.setMsg(response.getMessage());
                    return globeResponse;
                }
            } catch (ClientException e) {
                GlobeResponse<Object> globeResponse = new GlobeResponse<>();
                globeResponse.setCode(SystemConstants.FAIL_CODE);
                globeResponse.setMsg("发送失败");
                return globeResponse;
            }
        }
        GlobeResponse<Object> globeResponse = new GlobeResponse<>();
        globeResponse.setCode(SystemConstants.FAIL_CODE);
        globeResponse.setMsg("发送失败");
        return globeResponse;
    }

    /**
     * 众鑫短信
     */
    public String singleUSend(String phone, String vCode, Integer sendMode) {
        Map<String, String> params = new HashMap<String, String>();
        String account = "";
        String password = "";
        String content = "";
        String data = "";
        if (sendMode == 3) {
            account = "b030z2";
            password = MD5Utils.MD5Encode("12345678", "UTF-8");//"123456";
            content = "【众鑫娱乐】众鑫娱乐您的验证码是{" + vCode + "}，如非本人操作，请忽略此条短信。";
            data = "{\"smstype\":\"4\",\"clientid\":\"" + account + "\",\"password\":\"" + password + "\",\"mobile\":\"" + phone + "\",\"content\":\"" + content + "\"}";
        }
        if (sendMode == 5) {
            account = "b05zg5";
            password = MD5Utils.MD5Encode("12345678", "UTF-8");
            content = "【188游戏】您的验证码是{" + vCode + "}，如非本人操作，请忽略此条短信。";
            data = "{\"smstype\":\"4\",\"clientid\":\"" + account + "\",\"password\":\"" + password + "\",\"mobile\":\"" + phone + "\",\"content\":\"" + content + "\"}";
        }
        if (sendMode == 6) {
            account = "b062z2";
            password = MD5Utils.MD5Encode("12345678", "UTF-8");
            content = "【天朝网络】您的验证码是{" + vCode + "}，如非本人操作，请忽略此条短信。";
            data = "{\"smstype\":\"4\",\"clientid\":\"" + account + "\",\"password\":\"" + password + "\",\"mobile\":\"" + phone + "\",\"content\":\"" + content + "\"}";
        }
        if (sendMode == 7) {
            account = "b03ns3";
            password = MD5Utils.MD5Encode("12345678", "UTF-8");
            content = "【至尊网络】您的验证码是{" + vCode + "}，如非本人操作，请忽略此条短信。";
            data = "{\"smstype\":\"4\",\"clientid\":\"" + account + "\",\"password\":\"" + password + "\",\"mobile\":\"" + phone + "\",\"content\":\"" + content + "\"}";
        }
        if (sendMode == 9) {
            account = "b04v54";
            password = MD5Utils.MD5Encode("12345678", "UTF-8");
            content = "【金鼎国际】您的验证码是{" + vCode + "}，如非本人操作，请忽略此条短信。";
            data = "{\"smstype\":\"4\",\"clientid\":\"" + account + "\",\"password\":\"" + password + "\",\"mobile\":\"" + phone + "\",\"content\":\"" + content + "\"}";
        }
        if (sendMode == 10) {
            account = "b05r34";
            password = MD5Utils.MD5Encode("12345678", "UTF-8");
            content = "【广发手戏】您的验证码是{" + vCode + "}，如非本人操作，请忽略此条短信。";
            data = "{\"smstype\":\"4\",\"clientid\":\"" + account + "\",\"password\":\"" + password + "\",\"mobile\":\"" + phone + "\",\"content\":\"" + content + "\"}";
        }
        if (sendMode == 11) {
            account = "b07jn8";
            password = MD5Utils.MD5Encode("12345678", "UTF-8");
            content = "【金利来手游】您的验证码是{" + vCode + "}，如非本人操作，请忽略此条短信。";
            data = "{\"smstype\":\"4\",\"clientid\":\"" + account + "\",\"password\":\"" + password + "\",\"mobile\":\"" + phone + "\",\"content\":\"" + content + "\"}";
        }
        Map<String, String> map = new HashMap<String, String>();
        map.put("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
        map.put("Accept", "application/json;charset=utf-8");
        String sendUrl = "https://u.smsyun.cc/sms-partner/access/{" + account + "}/sendsms";
        log.info("发送的信息：" + data);
        log.info("sendUrl：" + sendUrl);
        return HttpRequest.sendPost(sendUrl, data, map);
    }

    /**
     * 众鑫短信 阿里云短信
     */
    public SendSmsResponse singleALYSend(String phone, String vCode, Integer sendMode) throws ClientException {
        //产品名称:云通信短信API产品,开发者无需替换
        String product = "Dysmsapi";
        //产品域名,开发者无需替换
        String domain = "dysmsapi.aliyuncs.com";

        // TODO 此处需要替换成开发者自己的AK(在阿里云访问控制台寻找)
        String accessKeyId = "";
        String accessKeySecret = "";
        if (sendMode == 4) {//众鑫
            accessKeyId = "LTAItRS97YCorosE";
            accessKeySecret = "Laenrf61drY2H4aljERFRlycEg8ATS";
        }
        if (sendMode == 8) {//联众
            accessKeyId = "LTAI7MgeSd8w0ji0";
            accessKeySecret = "wMwkKDHo5drZFRd3nUQpHgXe7jMKnw";
        }

        //可自助调整超时时间
        System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
        System.setProperty("sun.net.client.defaultReadTimeout", "10000");

        //初始化acsClient,暂不支持region化
        IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId, accessKeySecret);
        DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", product, domain);
        IAcsClient acsClient = new DefaultAcsClient(profile);

        //组装请求对象-具体描述见控制台-文档部分内容
        SendSmsRequest request = new SendSmsRequest();
        //必填:待发送手机号
        request.setPhoneNumbers(phone);
        if (sendMode == 4) {//众鑫
            //必填:短信签名-可在短信控制台中找到
            request.setSignName("众鑫");
            //必填:短信模板-可在短信控制台中找到
            request.setTemplateCode("SMS_170460451");
            //可选:模板中的变量替换JSON串,如模板内容为"亲爱的${name},您的验证码为${code}"时,此处的值为
            request.setTemplateParam("{\"code\":\"" + vCode + "\"}");
        }
        if (sendMode == 8) {//联众
            //必填:短信签名-可在短信控制台中找到
            request.setSignName("联众");
            //必填:短信模板-可在短信控制台中找到
            request.setTemplateCode("SMS_172530463");
            //可选:模板中的变量替换JSON串,如模板内容为"亲爱的${name},您的验证码为${code}"时,此处的值为
            request.setTemplateParam("{\"code\":\"" + vCode + "\"}");
        }
        //hint 此处可能会抛出异常，注意catch
        SendSmsResponse sendSmsResponse = acsClient.getAcsResponse(request);

        return sendSmsResponse;
    }


    /**
     * 手机绑定
     *
     * @return
     */
    @RequestMapping("/bindPhone")
    public GlobeResponse<Object> verificationCode(Integer userId, String password, String phone, String verifyCode,
   String realName,String bankNo,String bankName ) {
        BindPhoneVO bindPhoneVO =new BindPhoneVO();
        bindPhoneVO.setUserId(userId);
        bindPhoneVO.setPassword(password);
        bindPhoneVO.setPhone(phone);
        bindPhoneVO.setVerifyCode(verifyCode);
        bindPhoneVO.setRealName(realName);
        bindPhoneVO.setBankNo(bankNo);
        bindPhoneVO.setBankName(bankName);
        if (bindPhoneVO.getPassword() == null || bindPhoneVO.getPassword().length() < 6) {
            throw new GlobeException(SystemConstants.FAIL_CODE, "密码长度不能低于6位");
        }
        if (bindPhoneVO.getPhone() == null || bindPhoneVO.getPhone().length() < 11) {
            throw new GlobeException(SystemConstants.FAIL_CODE, "手机号格式错误");
        }
        String key = RedisKeyPrefix.getKey(bindPhoneVO.getPhone() + ":BindPhone");
        VerificationCodeVO verificationCode = redisDao.get(key, VerificationCodeVO.class);
        if (verificationCode == null) {
            throw new GlobeException(SystemConstants.FAIL_CODE, "验证码无效，请重新获取验证码");
        }
        if (!verificationCode.getCode().equals(bindPhoneVO.getVerifyCode())) {
            throw new GlobeException(SystemConstants.FAIL_CODE, "验证码错误");
        }
        if (System.currentTimeMillis() - verificationCode.getTimestamp() > 600000) {
            throw new GlobeException(SystemConstants.FAIL_CODE, "验证码已过期，请重新获取验证码");
        }
        if(!StringUtils.isBlank(bindPhoneVO.getBankNo())){
            if (bindPhoneVO.getBankNo().length()<16||bindPhoneVO.getBankNo().length()>19){
                throw new GlobeException(SystemConstants.FAIL_CODE, "银行卡位数不对!,请重新输入!");
            }
        }
        String pwd = MD5Encode(bindPhoneVO.getPassword(), "utf-8");
        bindPhoneVO.setPassword(pwd);
        VisitorBindResultVO visitorBindResult = this.accountsServiceClient.visitorBind(bindPhoneVO);
        if (visitorBindResult.getRet().intValue() != 0) {
            redisDao.delete(key);
            throw new GlobeException(SystemConstants.FAIL_CODE, visitorBindResult.getMsg());
        }
        GlobeResponse<Object> globeResponse = new GlobeResponse<>();
        globeResponse.setData(visitorBindResult.getScore());
        globeResponse.setMsg(visitorBindResult.getMsg());
        redisDao.delete(key);
        return globeResponse;
    }


    /**
     * 绑定或修改银行与支付宝信息
     *
     * @param userId
     * @param
     * @param phone
     * @param verifyCode
     * @return
     */
    @RequestMapping("/bindOrModifyPayInfo")
    private GlobeResponse<Object> bindOrModifyPayInfo(Integer userId, String bankRealName, String bankNo, String bankName, String bankAddress, String alipayRealName, String alipay,
                                                      String phone, String verifyCode, String zpass) {
        if (phone == null || phone.length() < 11) {
            throw new GlobeException(SystemConstants.FAIL_CODE, "手机号格式错误");
        }
        String key = RedisKeyPrefix.getKey(phone + ":ModifyBankInfo");
        VerificationCodeVO verificationCode = redisDao.get(key, VerificationCodeVO.class);
        if (verificationCode == null) {
            throw new GlobeException(SystemConstants.FAIL_CODE, "验证码无效，请重新获取验证码");
        }
        if (!verificationCode.getCode().equals(verifyCode)) {
            throw new GlobeException(SystemConstants.FAIL_CODE, "验证码错误");
        }
        if (System.currentTimeMillis() - verificationCode.getTimestamp() > 600000) {
            throw new GlobeException(SystemConstants.FAIL_CODE, "验证码已过期，请重新获取验证码");
        }
        String InsurePass = accountsServiceClient.getInsurePassInfo(userId);
        if (!InsurePass.toLowerCase().equals(zpass.toLowerCase())) {
            throw new GlobeException(SystemConstants.FAIL_CODE, "密码错误，请重新输入!");
        }
        Map<String, Object> resultMap = this.accountsServiceClient.bindOrModifyPayInfo(userId, bankRealName, bankNo, bankName, bankAddress, alipayRealName, alipay);
        if (((Integer) resultMap.get("ret")).intValue() != 0) {
            redisDao.delete(key);
            throw new GlobeException(SystemConstants.FAIL_CODE, resultMap.get("msg").toString());
        }
        GlobeResponse<Object> globeResponse = new GlobeResponse<>();
        redisDao.delete(key);
        return globeResponse;
    }

    @RequestMapping("/getAgentPay")
    private GlobeResponse<Object> getAgentPay(Integer agentId) {
        List<AgentInfoVO> list = agentServiceClient.getAgentPay(agentId);
        GlobeResponse<Object> globeResponse = new GlobeResponse<>();
        Map<String, Object> data = new HashMap<>();
        data.put("list", list);
        globeResponse.setData(data);
        return globeResponse;
    }

    @RequestMapping("/getPayList")
    private GlobeResponse<Object> getPayList(Integer userId, Integer agentId) {
        GlobeResponse<Object> globeResponse = new GlobeResponse<>();
        userId = userId == null ? 0 : userId;
        Map<String, List<PayInfoVO>> payList = treasureServiceClient.getPayList(userId);   //第三方充值渠道
        List<Object> companyList = treasureServiceClient.getCompanyPay(agentId);          //公司充值
        List<PayTypeList> lists = new ArrayList<>();
        if (companyList != null) {
            companyList.forEach(type -> {
                PayTypeList typeList = new PayTypeList();
                if ("AliPay".equals(type)) {
                    typeList.setId(0);
                    typeList.setPayType((String) type);
                }
                if ("WeChatPay".equals(type)) {
                    typeList.setId(1);
                    typeList.setPayType((String) type);
                }
                if ("BankPay".equals(type)) {
                    typeList.setId(2);
                    typeList.setPayType((String) type);
                }
                if ("CloudPay".equals(type)) {
                    typeList.setId(3);
                    typeList.setPayType((String) type);
                }
                if ("QQPay".equals(type)) {
                    typeList.setId(4);
                    typeList.setPayType((String) type);
                }
                if ("JinDongPay".equals(type)) {
                    typeList.setId(5);
                    typeList.setPayType((String) type);
                }
                lists.add(typeList);
            });
        }
        Map<String, Object> data = new HashMap<>();
        data.put("payList", payList);
        data.put("compayList", lists);

        globeResponse.setData(data);

        return globeResponse;
    }

    @RequestMapping("/getActivityList")
    private GlobeResponse<Object> getActivityList(Integer agentId) {
        if (agentId == null) {
            throw new GlobeException(SystemConstants.FAIL_CODE, "参数错误");
        }
        GlobeResponse<Object> globeResponse = new GlobeResponse<>();
        globeResponse.setData(nativeWebServiceClient.getActivityList(agentId));
        return globeResponse;
    }

    @RequestMapping("/getGameRecord")
    private GlobeResponse<Object> getGameRecord(int pageIndex, int pageSize, Integer userId, Integer kindId) {
        if (pageSize > 50) {
            pageSize = 50;
        }
        GlobeResponse<Object> globeResponse = new GlobeResponse<>();
        globeResponse.setData(this.treasureServiceClient.getGameRecord(pageIndex, pageSize, userId, kindId));
        return globeResponse;
    }

    @PostMapping("/addGameRecords")
    private GlobeResponse<Object> addGameRecord(@RequestBody JSONObject record) {
        GlobeResponse<Object> globeResponse = new GlobeResponse<>();
        JSONArray detailList = record.getJSONArray("detail");
        String detailString = detailList.toJSONString();
        long startTime = record.getLongValue("startTime") * 1000;
        long endTime = record.getLongValue("endTime") * 1000;
        String shortGameCode = record.getString("gameCode");
        Integer kindId = record.getInteger("kindId");
        Integer serverId = record.getInteger("serverId");
        String serverName = platformServiceClient.getServerName(serverId);
        for(Object d : detailList) {       
            JSONObject dJson = JSONObject.parseObject(d.toString());
            boolean isRobot = dJson.getBooleanValue("isRobot");
            if(isRobot) {
            	continue;
            }
            GameRecord gr = new GameRecord();
            Integer gameId = dJson.getInteger("gameId");
            gr.setPlayerId(gameId);
            gr.setServerId(serverId);
            gr.setKindId(kindId);
            gr.setGameHandCode(shortGameCode);
            gr.setGameCode(shortGameCode + "-" + dJson.getString("chairId"));
            gr.setStartTime(startTime);
            gr.setEndTime(endTime);
            gr.setGameName(serverName);
            gr.setScore(dJson.getBigDecimal("score"));
            gr.setRevenue(dJson.getBigDecimal("revenue"));
            if (gr.getScore() == null) {
                gr.setScore(BigDecimal.valueOf(0.00));
            } else if (gr.getRevenue() == null) {
                gr.setRevenue(BigDecimal.valueOf(0.00));
            }
            gr.setBetAmount(gr.getScore().add(gr.getRevenue()));
            AccountsInfoVO accountsInfo = this.accountsServiceClient.getUserInfoByGameId(gameId);
            gr.setAccount(accountsInfo.getAccount());            
            gr.setH5Account(accountsInfo.getH5Account());
            gr.setH5SiteCode(accountsInfo.getH5siteCode());
            gr.setDetail(detailString);
            //获取相对应游戏数据库表名
            String tableName = StringUtils.substringBeforeLast(StringUtils.substringBeforeLast(accountsServiceClient.getGameItem(gr.getKindId()), "Server"), "_");
            mongoTemplate.save(gr,"gameRecord_"+tableName);
            mongoTemplate.save(gr);
        }
        return globeResponse;
    }

//TODO 导数据专用
//    @RequestMapping("/initIp")
//    private GlobeResponse<Object>initIp(){
//        GlobeResponse<Object> globeResponse = new GlobeResponse<>();
//        String lineTxt = null;
//        try {
//    	    File file = new File("C:\\Users\\Owner\\Desktop\\45464.txt");
//    	    if(file.isFile() && file.exists()) {
//    	      InputStreamReader isr = new InputStreamReader(new FileInputStream(file), "utf-8");
//    	      BufferedReader br = new BufferedReader(isr);
//    	      while ((lineTxt = br.readLine()) != null) {
//    	        String[] lineSplit = lineTxt.split(" ");
//    	        String[] lineCells = new String[4];
//    	        int i = 0;
//    	        for(String s : lineSplit) {
//    	        	if(!s.equals("")) {
//    	        		lineCells[i++] = s;
//    	        		if(i == 4)
//    	        			break;
//    	        	}
//    	        }
//    	        lineCells[0] = StringUtil.delBom(lineCells[0]);
//    	        lineCells[1] = StringUtil.delBom(lineCells[1]);
//    	        long ip1Number = StringUtil.ip2Number(lineCells[0]);
//    	        long ip2Number = StringUtil.ip2Number(lineCells[1]);
//	        	IpDetailVO ipDetail = new IpDetailVO();
//	        	ipDetail.setIp(lineCells[0]);
//	        	ipDetail.setIp2(lineCells[1]);
//	        	ipDetail.setIpNumber(ip1Number);
//	        	ipDetail.setIpNumber2(ip2Number);
//	        	ipDetail.setRemark1(lineCells[2]);
//	        	ipDetail.setRemark2(lineCells[3]);
//	        	mongoTemplate.save(ipDetail);
//    	      }
//    	      br.close();
//    	    } else {
//    	      System.out.println("文件不存在!");
//    	    }
//
//    	  } catch (Exception e) {
//    		  System.err.println(lineTxt);
//    	    e.printStackTrace();
//    	  }
//  	    return globeResponse;
//    }

    @RequestMapping("/getIp")
    private GlobeResponse<Object> getIp(String ip) {
        GlobeResponse<Object> globeResponse = new GlobeResponse<>();
        //long now = System.currentTimeMillis();
//    	long number = StringUtil.ip2Number(ip);
//    	IpDetailVO data = mongoTemplate.findOne(new Query(Criteria.where("ipNumber").lte(number).and("ipNumber2").gte(number)), IpDetailVO.class);
//    	//System.err.println(System.currentTimeMillis()-now);
//    	globeResponse.setData(data == null ? "未知" : data.getRemark1());
        //globeResponse.setData(data == null ? "未知" : data);
        return globeResponse;
    }


    /**
     * 默认发送验证码
     *
     * @param phone
     * @param code
     * @return
     */
    private String sendCode(String phone, String code) {
        String message = "您的验证码是： " + code + "。请不要把验证码泄露给其他人。";
        String param = "account=" + phoneName + "&password=" + phonePwd + "&mobile=" + phone + "&content=" + message;
        String resTxt = HttpRequest.sendPost(phonePostUrl, param);
        return resTxt;
    }

    /**
     * 完美和3D发送验证码
     */
    public String singleSend(String phone, String vCode, Integer sendMode) throws YunpianException {
        Map<String, String> params = new HashMap<String, String>();
        if (sendMode == 1) {
            params.put("apikey", "ef82551b34a5f2edffb5d8a12120064a");
            params.put("text", "【完美娱乐】您的验证码是" + vCode + "。如非本人操作，请忽略本短信");
        }
        if (sendMode == 2) {
            params.put("apikey", "5d3d8fb1a1cd3401b1987d7301fddf0d");
            params.put("text", "【3D网络】您的验证码是" + vCode + "。如非本人操作，请忽略本短信");
        }
        params.put("mobile", phone);
        return post("https://sms.yunpian.com/v2/sms/single_send.json", params);
    }

    /**
     * 支付跳转
     * <p>
     * 原account 换成userId
     *
     * @param userId  用户ID
     * @param amount  充值金额
     * @param qudaoId 渠道ID
     * @return
     */
    @RequestMapping("/payPageLoad")
    private String payPageLoad(int userId, String account, BigDecimal amount, int qudaoId, HttpServletRequest request) throws YunpianException {
        if (amount.equals(null) || qudaoId <= 0 || userId <= 0 || StringUtil.isEmpty(account)) {
            throw new GlobeException(SystemConstants.FAIL_CODE, "参数错误!");
        }
        StringBuffer url = request.getRequestURL();
        String tempContextUrl = url.delete(url.length() - request.getRequestURI().length(), url.length()).toString();
        StringBuilder html = new StringBuilder();
        html.append("<!DOCTYPE html><html><head><title>支付页面</title><style>.tabPages{margin-top:150px;text-align:center;display:block; border:3px solid #d9d9de; padding:30px; font-size:14px;");
        html.append("}</style></head><body>");
        html.append("<div id=\"Content\"><div class=\"tabPages\">我们正在为您连接银行，请稍等......</div></div><form name=\"sendForm\" action=\"" + tempContextUrl + "/mobileInterface/payPageLoad/submit\" method=\"post\">");
        html.append("<input type=\"hidden\" name=\"userId\"  value=\"" + userId + "\"><input type=\"hidden\" name=\"account\"  value=\"" + account + "\">");
        html.append("<input type=\"hidden\" name=\"qudaoId\"  value=\"" + qudaoId + "\"><input type=\"hidden\" name=\"amount\"  value=\"" + amount + "\">");
        html.append("</form><script>document.sendForm.submit();</script></body></html>");
        return html.toString();
    }

    /**
     * 支付跳转
     * <p>
     * 原account 换成userId
     *
     * @param userId  用户ID
     * @param amount  充值金额
     * @param qudaoId 渠道ID
     * @return
     */
    @RequestMapping("/payPageLoad/submit")
    private String payPageLoadSubmit(int userId, String account, BigDecimal amount, int qudaoId, HttpServletRequest request) throws YunpianException {
        if (amount == null || qudaoId <= 0 || userId <= 0 || StringUtil.isEmpty(account)) {
            throw new GlobeException(SystemConstants.FAIL_CODE, "参数错误!");
        }

        ViewPayInfoVO payInfoVO = treasureServiceClient.getPayInfo(qudaoId);
        TpayOwnerInfoVO payOwnerInfo = treasureServiceClient.getPayOwnerInfo();

        OnLineOrderVO onLineOrderVO = new OnLineOrderVO();
        onLineOrderVO.setOrderId(GetOrderIDByPrefix("e"));  //订单标识
        onLineOrderVO.setOperUserId(userId);                // 操作用户
        onLineOrderVO.setAccounts(account);                 //充值用户
        onLineOrderVO.setOrderAmount(amount);               //订单金额
        onLineOrderVO.setIPAddress(getIpAddress(request));  // 支付地址
        onLineOrderVO.setShareId(payInfoVO.getShareId());
        onLineOrderVO.setPayInfoId(payInfoVO.getId());
        onLineOrderVO.setPayType(payInfoVO.getPayType());

        //保存订单
        HashMap map = treasureServiceClient.getRequestOrder(onLineOrderVO);
        Integer ret = (Integer) map.get("ret");
        String strErrorDescribe = (String) map.get("strErrorDescribe");
        String mag = "";
        if (ret == 1) {
            mag = strErrorDescribe;
            throw new GlobeException(SystemConstants.FAIL_CODE, mag);
        }
        if (ret == 2) {
            mag = strErrorDescribe;
            throw new GlobeException(SystemConstants.FAIL_CODE, mag);
        }
        if (ret == 3) {
            mag = strErrorDescribe;
            throw new GlobeException(SystemConstants.FAIL_CODE, mag);
        }
        if (ret == 4) {
            mag = strErrorDescribe;
            throw new GlobeException(SystemConstants.FAIL_CODE, mag);

        } else {
//            StringBuffer url = request.getRequestURL();
//            String tempContextUrl = url.delete(url.length() - request.getRequestURI().length(), url.length()).toString();
            String tempContextUrl = mobileInterfaceUrl;
            Map<String, String> data = new LinkedHashMap<>();
            data.put("amount", amount.toString());
            data.put("backUrl", tempContextUrl + "/mobileInterface/payCallBack");
            data.put("memberId", payInfoVO.getMemberId());
            data.put("memberKey", payInfoVO.getMemberKey());
            data.put("ownerId", payOwnerInfo.getOwnerId());
            data.put("ownerOrderId", onLineOrderVO.getOrderId());
            data.put("payType", payInfoVO.getPayTypeCode());
            data.put("appId", payInfoVO.getAppId());
            String params = getParam(data);
            String sign = "amount=" + data.get("amount") + "&backUrl=" + data.get("backUrl") + "&memberId=" + data.get("memberId") +
                    "&memberKey=" + data.get("memberKey") + "&ownerId=" + data.get("ownerId") + "&ownerOrderId=" + data.get("ownerOrderId") +
                    "&payType=" + data.get("payType") + "&appId=" + data.get("appId");
            sign = MD5Encode(sign + payOwnerInfo.getOwnerKey(), "utf-8");
            params += "&ownerSign=" + sign;
            log.info("发送到中转中心：" + payInfoVO.getSendUrl() + "?" + params);
            mag = HttpRequest.sendPost(payInfoVO.getSendUrl(), params);
            return mag;


        }
    }

    /**
     * 获取链接参数
     *
     * @param paramMap
     * @return
     */
    private String getParam(Map<String, String> paramMap) {
        String str = "";
        try {
            for (String key : paramMap.keySet()) {
                String val = paramMap.get(key);
                if (val == null || val.equals("")) {
                    continue;
                }
                if (str.length() == 0) {
                    str = key + "=" + URLEncoder.encode(val, "UTF-8");
                } else {
                    str += "&" + key + "=" + URLEncoder.encode(val, "UTF-8");
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return str;
    }

    /**
     * 支付回调
     *
     * @return
     */
    @RequestMapping("/payCallBack")
    @ResponseBody
    private Object payCallBack(String orderId, String ownerOrderId, String orderType, String amount, String ownerId, String ownerSign, HttpServletRequest request) {
        log.info("接收到订单支付结果：" + orderId + ", " + ownerOrderId + ", " + orderType + ", " + amount + ", " + ownerId + ", " + ownerSign);
        TpayOwnerInfoVO payOwnerInfo = treasureServiceClient.getPayOwnerInfo();
        String param = "orderId=" + orderId + "&ownerOrderId=" + ownerOrderId + "&orderType="
                + orderType + "&amount=" + amount + "&ownerId=" + ownerId;
        String md5Key = MD5Encode(param + payOwnerInfo.getOwnerKey(), "utf-8");
        if (!md5Key.toUpperCase().equals(ownerSign.toUpperCase())) {
            log.info("签名错误");
            return "success";
        }
        if (!orderType.equals("2")) {
            log.info("支付失败");
            return "success";
        }

        ShareDetailInfoVO shareDetailInfoVO = new ShareDetailInfoVO();
        shareDetailInfoVO.setOrderId(ownerOrderId);
        shareDetailInfoVO.setIpAddress(getIpAddress(request));
        shareDetailInfoVO.setPayAmount(new BigDecimal(amount));
        shareDetailInfoVO.setMerchantOrderId(orderId);
        Map<String, Object> data = treasureServiceClient.filliedOnline(shareDetailInfoVO);
        Object userId = data.get("userId");
        Object score = data.get("score");
        Object insureScore =data.get("insureScore");
        Object level =data.get("vipLevel");
        String msg =  "{\"msgid\":7,\"userId\":" +userId + ", \"score\":" + score + ",\"insuranceScore\":"+insureScore +
                ", \"VipLevel\":"+level+ ", \"type\":"+1+ ", \"Charge\":"+amount+"}";
        log.info("调用金额变更指令:{}, 返回：" + HttpRequest.sendPost(this.serverUrl, msg), msg);
        return "success";
    }

    /**
     * 根据用户标识与密码获取网关地址
     *
     * @param account，passWord，type
     * @param passWord              password = MD5Encode(password, "utf-8");
     *                              Type 0 账户  2 游客  5 微信  6第三方登录
     * @return
     */
    @RequestMapping("/getGateway")
    private Map<String, Object> getGatewayInfo(String account, String passWord, int type, Integer agentId) {
        if (type < 0) {
            throw new GlobeException(SystemConstants.FAIL_CODE, "参数错误，请确认");
        }
        if (type != 0) {
            passWord = MD5Encode(passWord, "utf-8");
        }

        if (agentId == null) {
            agentId = 0;
        }
        if (type == 6) {
            String tokenSaveKey = RedisKeyPrefix.getTokenKey(account);
            AccountsInfoVO ai = redisDao.get(tokenSaveKey, AccountsInfoVO.class);
            if (ai == null) {
                throw new GlobeException(SystemConstants.FAIL_CODE, "未查到相关用户信息，请确认!");
            }
            account = ai.getAccount();
        }

        GatewayInfo gateway = treasureServiceClient.getGatewayInfo(account, passWord, type, agentId);

        Map<String, Object> data = new HashMap<>();
        // String updateUrl = gateway.getUpdateAddress();
//        if (StringUtils.isBlank(updateUrl)) {
//            data.put("HOT_UPDATE_URL", null);
//        } else {
//            String[] update = updateUrl.split(",");
//            data.put("HOT_UPDATE_URL", update);
//        }
        if (StringUtils.isBlank(gateway.getGateway())) {
            data.put("GATE_SERVER_LIST", null);
        } else {
            String[] gate = gateway.getGateway().split(",");
            data.put("GATE_SERVER_LIST", gate);
        }

        if (StringUtils.isBlank(gateway.getWebAddress())) {
            data.put("WEB_HTTP_URL_TEST", null);
        } else {
            String[] web = gateway.getWebAddress().split(",");
            data.put("WEB_HTTP_URL_TEST", web);
        }

        return data;
    }

    /**
     * 验证 手机获取验证发是否有效
     *
     * @param phone
     * @param verifyCode
     * @return
     */
    @RequestMapping("/verificationPhoneCode")
    private GlobeResponse<Object> verificationPhoneCode(String phone, String verifyCode) {
        if (phone == null || phone.length() < 11) {
            throw new GlobeException(SystemConstants.FAIL_CODE, "手机号格式错误");
        }
        String key = RedisKeyPrefix.getKey(phone + ":BindPhone");
        VerificationCodeVO verificationCode = redisDao.get(key, VerificationCodeVO.class);
        if (verificationCode == null) {
            throw new GlobeException(SystemConstants.FAIL_CODE, "验证码无效，请重新获取验证码");
        }
        if (!verificationCode.getCode().equals(verifyCode)) {
            throw new GlobeException(SystemConstants.FAIL_CODE, "验证码错误");
        }
        if (System.currentTimeMillis() - verificationCode.getTimestamp() > 600000) {
            throw new GlobeException(SystemConstants.FAIL_CODE, "验证码已过期，请重新获取验证码");
        }
        // redisDao.delete(key);
        // redisDao.delete(key);
        GlobeResponse<Object> globeResponse = new GlobeResponse<>();
        return globeResponse;
    }

    /**
     * 更换绑定手机
     *
     * @param userId
     * @param originalPhone
     * @param replacePhone
     * @param verifyCode
     * @return
     */
    @RequestMapping("/replacePhoneCode")
    private GlobeResponse<Object> replacePhoneCode(Integer userId, String originalPhone, String originalCode, String replacePhone, String verifyCode) {
        if (replacePhone == null || replacePhone.length() < 11) {
            throw new GlobeException(SystemConstants.FAIL_CODE, "手机号格式错误");
        }
        if (originalPhone == null || originalPhone.length() < 11) {
            throw new GlobeException(SystemConstants.FAIL_CODE, "手机号格式错误");
        }
        if (replacePhone.equals(originalPhone)) {
            throw new GlobeException(SystemConstants.FAIL_CODE, "更换的手机号，不允许与原手机号码一致!");
        }
        //验证原手机号码是否通过
        String phoneKey = RedisKeyPrefix.getKey(originalPhone + ":BindPhone");
        VerificationCodeVO verificationCodeKey = redisDao.get(phoneKey, VerificationCodeVO.class);
        if (!verificationCodeKey.getCode().equals(originalCode)) {
            throw new GlobeException(SystemConstants.FAIL_CODE, "原验证码错误");
        }


        //执行更换手机号
        String key = RedisKeyPrefix.getKey(replacePhone + ":BindPhone");
        VerificationCodeVO verificationCode = redisDao.get(key, VerificationCodeVO.class);
        if (verificationCode == null) {
            throw new GlobeException(SystemConstants.FAIL_CODE, "验证码无效，请重新获取验证码");
        }
        if (!verificationCode.getCode().equals(verifyCode)) {
            throw new GlobeException(SystemConstants.FAIL_CODE, "验证码错误");
        }
        if (System.currentTimeMillis() - verificationCode.getTimestamp() > 600000) {
            throw new GlobeException(SystemConstants.FAIL_CODE, "验证码已过期，请重新获取验证码");
        }
        VisitorBindResultVO visitorBindResult = this.accountsServiceClient.replacePhoneCode(userId, replacePhone);
        if (visitorBindResult.getRet().intValue() != 0) {
            redisDao.delete(key);
            redisDao.delete(phoneKey);
            throw new GlobeException(SystemConstants.FAIL_CODE, visitorBindResult.getMsg());
        }
        GlobeResponse<Object> globeResponse = new GlobeResponse<>();
        globeResponse.setData(visitorBindResult.getScore());
        globeResponse.setMsg(visitorBindResult.getMsg());
        redisDao.delete(key);
        redisDao.delete(phoneKey);
        return globeResponse;
    }

    @RequestMapping("/updateMerchantOrderId")
    public String updateMerchantOrderId(String ownerOrderId, String merchantOrderId) {
        this.treasureServiceClient.updateMerchantOrderId(ownerOrderId, merchantOrderId);
        return "ok";
    }

    @RequestMapping("/updatePassagewayResponse")
    public String updatePassagewayResponse(String ownerOrderId, String passagewayResponse) {
        this.treasureServiceClient.updatePassagewayResponse(ownerOrderId, passagewayResponse);
        return "ok";
    }

    /**
     * 是否开启验证功能
     *
     * @return
     */
    @RequestMapping("/isOpenVerification")
    public Integer isOpenVerification(Integer agentId) {

        Integer isopen = agentServiceClient.isOpenVerification(agentId, AgentSystemEnum.VerificationIsOpen.getName());

        return isopen;
    }

    /**
     * 游戏玩家反馈建议
     */
    @RequestMapping("/getFeedbackInfo")
    public GlobeResponse getFeedbackInfo(Integer userId) {
        List<GameFeedbackVO> list = platformServiceClient.getFeedbackInfo(userId);
        GlobeResponse<Object> globeResponse = new GlobeResponse<>();
        globeResponse.setData(list);
        return globeResponse;
    }

    /**
     * 游戏玩家反馈建议
     */
    @RequestMapping("/insertFeedbackInfo")
    public GlobeResponse insertFeedbackInfo(Integer userId, Integer gameId, Integer feedBackType, String feedBackTxt, Integer serverId) throws UnsupportedEncodingException {
        if (userId == null || gameId == 0) {
            throw new GlobeException(SystemConstants.FAIL_CODE, "参数错误，请核实!");
        }
        if (serverId == null) {
            serverId = 0;
        }
        if (StringUtils.isBlank(feedBackTxt)) {
            throw new GlobeException(SystemConstants.FAIL_CODE, "请输入反馈内容!");
        }
        //对feedBack解码
        // String text = getBase64(feedBackTxt);
        Boolean flag = platformServiceClient.insertFeedbackInfo(userId, gameId, feedBackType, feedBackTxt, serverId);
        GlobeResponse<Object> globeResponse = new GlobeResponse<>();
        if (flag == false) {
            throw new GlobeException(SystemConstants.FAIL_CODE, "网络断开，请重新提交!");
        }
        return globeResponse;
    }

    /**
     * 游戏玩家反馈建议
     */
    @RequestMapping("/deFeedbackInfo")
    public GlobeResponse deFeedbackInfo(Integer id) {
        if (id == null || id == 0) {
            throw new GlobeException(SystemConstants.FAIL_CODE, "参数错误，请核实!");
        }
        Boolean flag = platformServiceClient.deFeedbackInfo(id);
        GlobeResponse<Object> globeResponse = new GlobeResponse<>();
        if (flag == false) {
            throw new GlobeException(SystemConstants.FAIL_CODE, "网络断开，请重新提交!");
        }
        return globeResponse;
    }

    /**
     * 查询客服信息
     *
     * @param agentId
     * @return
     */
    @RequestMapping("/getCustomerInfo")
    private GlobeResponse<Object> getCustomerInfo(Integer agentId) {
        if (agentId == null) {
            throw new GlobeException(SystemConstants.FAIL_CODE, "参数错误");
        }
        List<CustomerServiceConfigVO> customers = platformServiceClient.getCustomerInfo(agentId);
        List<ProblemConfigVO> problems = platformServiceClient.getProblemConfigInfo(agentId);
        Map<String, Object> data = new HashMap<>();
        data.put("customers", customers);
        data.put("problems", problems);
        GlobeResponse<Object> globeResponse = new GlobeResponse<>();
        globeResponse.setData(data);
        return globeResponse;
    }

    /**
     * 查询业主兑换记录
     *
     * @param agentId
     * @return
     */
    @RequestMapping("/getMinBalanceInfo")
    private GlobeResponse<Object> getMinBalanceInfo(Integer agentId) {
        if (agentId == null) {
            throw new GlobeException(SystemConstants.FAIL_CODE, "参数错误");
        }
        GoldExchangeVO goldExchangeVO = accountsServiceClient.getMinBalanceInfo(agentId);
        Map<String, Object> data = new HashMap<>();
        data.put("minBalance", goldExchangeVO.getMinBalance());  //最低出售金币
        data.put("counterFee", goldExchangeVO.getCounterFee() * 100);   //支付宝提现手续费
        data.put("minCounterFee", goldExchangeVO.getMinCounterFee());   //支付宝提现最低手续费
        data.put("bankCounterFee", goldExchangeVO.getBankCounterFee().multiply(new BigDecimal(100)));   //银行卡提现最低手续费
        data.put("minBankCounterFee", goldExchangeVO.getMinBankCounterFee());   //银行卡提现最低手续费
        data.put("isOpenAli", goldExchangeVO.getIsOpenAli());    //是否开启支付宝 0 开启  1是禁用
        data.put("IsOpenBank", goldExchangeVO.getIsOpenBank()); //是否开启银行卡  0 开启  1是禁用
        GlobeResponse<Object> globeResponse = new GlobeResponse<>();
        globeResponse.setData(data);
        return globeResponse;
    }

    /**
     * 查询业主联系方式
     *
     * @param agentId
     * @return
     */
    @RequestMapping("/getAgentContactInfo")
    private GlobeResponse<Object> getAgentContactInfo(Integer agentId) {
        if (agentId == null) {
            throw new GlobeException(SystemConstants.FAIL_CODE, "参数错误");
        }
        AgentAccVO accVO = agentServiceClient.getAgentContactInfo(agentId);
        Map<String, Object> data = new HashMap<>();
        data.put("qq", accVO.getQq());
        data.put("weChat", accVO.getWeChat());
        GlobeResponse<Object> globeResponse = new GlobeResponse<>();
        globeResponse.setData(data);
        return globeResponse;
    }

    /**
     * 查询代理客服
     *
     * @param agentId
     * @return
     */
    @RequestMapping("/getAgentCustomerServiceInfo")
    private GlobeResponse<Object> getAgentCustomerServiceInfo(Integer agentId) {
    	if (agentId == null) {
            throw new GlobeException(SystemConstants.FAIL_CODE, "参数错误");
        }
        List<CustomerServiceConfigVO> customers = platformServiceClient.getAgentCustomerServiceInfo(agentId);
        Map<String, Object> data = new HashMap<>();
        data.put("customers", customers);
        GlobeResponse<Object> globeResponse = new GlobeResponse<>();
        globeResponse.setData(data);
        return globeResponse;
    }

    /**
     * 查询银行卡类型
     *
     * @param agentId
     * @return
     */
    @RequestMapping("/getBankCardTypeInfo")
    private GlobeResponse<Object> getBankCardTypeInfo(Integer agentId) {
    	if (agentId == null) {
            throw new GlobeException(SystemConstants.FAIL_CODE, "参数错误");
        }
        List<BankCardTypeVO> customers = agentServiceClient.getBankCardTypeInfo(agentId);
        Map<String, Object> data = new HashMap<>();
        data.put("bankCards", customers);
        GlobeResponse<Object> globeResponse = new GlobeResponse<>();
        globeResponse.setData(data);
        return globeResponse;
    }


    /**
     * 查询幸运转盘
     *
     * @param agentId
     * @return
     */
    @RequestMapping("/getLucky")
    private GlobeResponse<Object> getLucky(Integer agentId) {
    	if (agentId == null) {
            throw new GlobeException(SystemConstants.FAIL_CODE, "参数错误");
        }
    	LuckyTurntableConfigurationVO Lucky = treasureServiceClient.getLucky(agentId);
        Map<String, Object> data = new HashMap<>();
        data.put("Lucky", Lucky);
        GlobeResponse<Object> globeResponse = new GlobeResponse<>();
        globeResponse.setData(data);
        return globeResponse;
    }
}
