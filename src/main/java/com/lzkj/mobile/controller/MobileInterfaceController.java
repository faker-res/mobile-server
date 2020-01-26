package com.lzkj.mobile.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.lzkj.mobile.client.*;
import com.lzkj.mobile.config.AgentSystemEnum;
import com.lzkj.mobile.config.SiteConfigKey;
import com.lzkj.mobile.config.SystemConfigKey;
import com.lzkj.mobile.config.SystemConstants;
import com.lzkj.mobile.exception.GlobeException;
import com.lzkj.mobile.exception.YunpianException;
import com.lzkj.mobile.mongo.GameRecord;
import com.lzkj.mobile.redis.RedisDao;
import com.lzkj.mobile.redis.RedisKeyPrefix;
import com.lzkj.mobile.redis.RedisLock;
import com.lzkj.mobile.schedule.PayLineCheckJob;
import com.lzkj.mobile.util.*;
import com.lzkj.mobile.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static com.lzkj.mobile.config.AwardOrderStatus.getDescribe;
import static com.lzkj.mobile.util.HttpUtil.post;
import static com.lzkj.mobile.util.IpAddress.getIpAddress;
import static com.lzkj.mobile.util.MD5Utils.MD5Encode;
import static com.lzkj.mobile.util.MD5Utils.getAllFields;
import static com.lzkj.mobile.util.PayUtil.GetOrderIDByPrefix;

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

    @Resource(name = "gameMongoTemplate")
    private MongoTemplate mongoTemplate;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;


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
//        List<ThirdKindConfigVO> thirdList =  platformServiceClient.getMobileThirdKindList(Integer.valueOf(agentId));
        GameListVO data = new GameListVO();
        data.setValid(true);
        data.setDownloadUrl(value2);
        data.setDebugUrl(value5);
        data.setWxLogon(status2);
        data.setIsOpenCard(systemStatusInfo == null ? 1 : systemStatusInfo.getStatusValue().intValue());
        data.setGameList(mobileKindList);
//        data.setThirdGameList(thirdList);
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
                    getBankRecordResponse.setTradeTypeDescription("余额宝存款");
                    getBankRecordResponse.setTransferAccounts("");
                } else if (l.getTradeType() == 2) {
                    getBankRecordResponse.setTradeTypeDescription("余额宝取款");
                    getBankRecordResponse.setTransferAccounts("");
                } else {
                    if (l.getSourceUserId() == userId) {
                        BigDecimal swapScore = l.getSwapScore().negate();
                        l.setSwapScore(swapScore);
                        getBankRecordResponse.setTradeTypeDescription("余额宝转账");
                        AccountsInfoVO accountsInfo = accountsServiceClient.getAccountsInfo(l.getTargetUserId());
                        getBankRecordResponse.setTransferAccounts(String.valueOf(accountsInfo.getGameId()));
                        getBankRecordResponse.setSwapScore(l.getSwapScore());
                    } else {
                        getBankRecordResponse.setTradeTypeDescription("余额宝收款");
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
        long startMillis = System.currentTimeMillis();
        log.info("/adsNotice,参数agentId={}", agentId);
        GlobeResponse<List<NewsVO>> globeResponse = new GlobeResponse<>();
        globeResponse.setData(nativeWebServiceClient.getGameNotice(1, agentId));
        log.info("/adsNotice,耗时:{}", System.currentTimeMillis() - startMillis);
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
     * 8 联众短信        9 为金鼎国际短信   10 为广发短信   11 为金利来   12为大发  13为117   14 为开元   15 百家  26开元
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
        if (sendMode == 44) {
            vCode = "123456";
            GlobeResponse<Object> globeResponse = new GlobeResponse<>();
            String key = RedisKeyPrefix.getKey(phone + ":" + type);
            VerificationCodeVO verificationCode = new VerificationCodeVO();
            verificationCode.setCode(vCode);
            redisDao.set(key, verificationCode);
            redisDao.expire(key, 11, TimeUnit.MINUTES);
            return globeResponse;
        }
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

        if (sendMode == 4 || sendMode == 8 || sendMode == 12 || sendMode == 13 || sendMode == 14 || sendMode == 15 || sendMode == 26) {
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
            content = "【广发手游】您的验证码是{" + vCode + "}，如非本人操作，请忽略此条短信。";
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
        if (sendMode == 12) {//大发
            accessKeyId = "LTAI4FqyxG1Ke9Ba5Et1k7dT";
            accessKeySecret = "O7OhzgufrypeJ8lgJzKGdXe45IiPLE";
        }
        if (sendMode == 13) {//117
            accessKeyId = "LTAI4FxhXrZEGBT3YA8JMUTz";
            accessKeySecret = "4ZWAr0ORKMPlUHyoPg9hvRCWHao7p2";
        }
        if (sendMode == 14) {//开元
            accessKeyId = "LTAI4FsUERw7ZHDiEbJJV2X5";
            accessKeySecret = "v0mNaai7xXdETrpVnPkrsHba8Iwkpa";
        }
        if (sendMode == 26) {//开元
        	accessKeyId = "LTAI4Fxr5Py9og1m89HigKAQ";
            accessKeySecret = "sChKu5H5Hje5nDKuWu1yDOTF7UkJax";
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
        if (sendMode == 12) {//大发
            //必填:短信签名-可在短信控制台中找到
            request.setSignName("大发");
            //必填:短信模板-可在短信控制台中找到
            request.setTemplateCode("SMS_177248087");
            //可选:模板中的变量替换JSON串,如模板内容为"亲爱的${name},您的验证码为${code}"时,此处的值为
            request.setTemplateParam("{\"code\":\"" + vCode + "\"}");
        }
        if (sendMode == 13) {//117
            //必填:短信签名-可在短信控制台中找到
            request.setSignName("117APP");
            //必填:短信模板-可在短信控制台中找到
            request.setTemplateCode("SMS_177253376");
            //可选:模板中的变量替换JSON串,如模板内容为"亲爱的${name},您的验证码为${code}"时,此处的值为
            request.setTemplateParam("{\"code\":\"" + vCode + "\"}");
        }
        if (sendMode == 14) {//开元
            //必填:短信签名-可在短信控制台中找到
            request.setSignName("开元");
            //必填:短信模板-可在短信控制台中找到
            request.setTemplateCode("SMS_178766749");
            //可选:模板中的变量替换JSON串,如模板内容为"亲爱的${name},您的验证码为${code}"时,此处的值为
            request.setTemplateParam("{\"code\":\"" + vCode + "\"}");
        }
        if(sendMode == 26) {//开元
        	//必填:短信签名-可在短信控制台中找到
            request.setSignName("开元");
            //必填:短信模板-可在短信控制台中找到
            request.setTemplateCode("SMS_182671827");
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
                                                  String realName, String bankNo, String bankName) {
        BindPhoneVO bindPhoneVO = new BindPhoneVO();
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
        if (!StringUtils.isBlank(bindPhoneVO.getBankNo())) {
            if (bindPhoneVO.getBankNo().length() < 16 || bindPhoneVO.getBankNo().length() > 19) {
                throw new GlobeException(SystemConstants.FAIL_CODE, "银行卡位数不对!,请重新输入!");
            }

            Integer count = accountsServiceClient.getUserBankInformation(bindPhoneVO.getBankNo());
            if (count >= 1) {
                throw new GlobeException(SystemConstants.FAIL_CODE, "此银行卡已被绑定!,请重新输入!");
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
     * 修改绑定密码
     */
    @RequestMapping("/resetInsurePwd")
    public GlobeResponse<Object> resetInsurePwd(Integer userId, String phone, String oldPwd, String newPwd, String verifyCode) {
        String key = RedisKeyPrefix.getKey(phone + ":BindPhone");
        VerificationCodeVO verificationCode = redisDao.get(key, VerificationCodeVO.class);
       /* if (verificationCode == null) {
            throw new GlobeException(SystemConstants.FAIL_CODE, "验证码无效，请重新获取验证码");
        }
        if (!verificationCode.getCode().equals(verifyCode)) {
            throw new GlobeException(SystemConstants.FAIL_CODE, "验证码错误");
        }
        if (System.currentTimeMillis() - verificationCode.getTimestamp() > 600000) {
            throw new GlobeException(SystemConstants.FAIL_CODE, "验证码已过期，请重新获取验证码");
        }*/
        if (oldPwd.equals(newPwd)) {
            throw new GlobeException(SystemConstants.FAIL_CODE, "新密码与旧密码一致");
        }
        String n = MD5Utils.MD5Encode(newPwd, "UTF-8").toUpperCase();
        String o = MD5Utils.MD5Encode(oldPwd, "UTF-8").toUpperCase();
        Map<String, Object> resultMap = this.accountsServiceClient.resetInsurePwd(userId, o, n);
        if (((Integer) resultMap.get("ret")).intValue() != 0) {
            redisDao.delete(key);
            throw new GlobeException(SystemConstants.FAIL_CODE, resultMap.get("msg").toString());
        }
        GlobeResponse<Object> globeResponse = new GlobeResponse<>();
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

    /**
     * 获取充值列表
     *
     * @param userId
     * @param agentId
     * @return
     */
    @RequestMapping("/getPayList")
    private GlobeResponse<Object> getPayList(Integer userId, Integer agentId) {
        long startMillis = System.currentTimeMillis();
        log.info("/getPayList,参数:userId={},agentId={}", userId, agentId);
        GlobeResponse<Object> globeResponse = new GlobeResponse<>();
        userId = userId == null ? 0 : userId;
        Map<String, List<PayInfoVO>> payList = treasureServiceClient.getPayList(userId, agentId);   //第三方充值渠道
        List<CompanyPayVO> companyList = treasureServiceClient.getCompanyPay(userId, agentId);          //公司充值
        if (companyList != null && companyList.size() > 0) {
            companyList.forEach(type -> {
                if ("AliPay".equals(type.getPayType())) {
                    type.setPayId(0);
                }
                if ("WeChatPay".equals(type.getPayType())) {
                    type.setPayId(1);
                }
                if ("BankPay".equals(type.getPayType())) {
                    type.setPayId(2);
                }
                if ("CloudPay".equals(type.getPayType())) {
                    type.setPayId(3);
                }
                if ("QQPay".equals(type.getPayType())) {
                    type.setPayId(4);
                }
                if ("JinDongPay".equals(type.getPayType())) {
                    type.setPayId(5);
                }
                if("redPwd".equals(type.getPayType())){
                    type.setPayId(6);
                }
            });
        }
        Map<String, Object> data = new HashMap<>();
        data.put("payList", payList);
        data.put("compayList", companyList);

        globeResponse.setData(data);
        log.info("/getPayList,耗时:{}", System.currentTimeMillis() - startMillis);
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

    @RequestMapping("/addException")
    public GlobeResponse<Object> addException(String time, String yeZhu, String error, String errorMessage, String file, String line, String message, String httpSend, String scene, String socketMainCode, String socketSubCode
            , String eventName, String newHandle, String kindId, String platform) {
        GlobeResponse globeResponse = new GlobeResponse();
       /* if () {
            throw new GlobeException(SystemConstants.FAIL_CODE, "参数不能为空");
        }*/
        GameException gameException = new GameException();
        gameException.setTime(time);
        gameException.setYeZhu(yeZhu);
        gameException.setError(error);
        gameException.setErrorMessage(errorMessage);
        gameException.setFile(file);
        gameException.setLine(line);
        gameException.setMessage(message);
        gameException.setHttpSend(httpSend);
        gameException.setScene(scene);
        gameException.setSocketMainCode(socketMainCode);
        gameException.setSocketSubCode(socketSubCode);
        gameException.setEventName(eventName);
        gameException.setNewHandle(newHandle);
        gameException.setKindId(kindId);
        gameException.setPlatform(platform);
        Boolean flag = true;
        try {
            mongoTemplate.save(gameException, "Exception");
        } catch (Exception e) {
            flag = false;
        }

        globeResponse.setData(flag);
        return globeResponse;
    }

    @PostMapping("/addGameRecord")
    public GlobeResponse<Object> addGameRecord(@RequestBody JSONObject record) {
        GlobeResponse<Object> globeResponse = new GlobeResponse<>();
        JSONArray detailList = record.getJSONArray("detail");
        String detailString = detailList.toJSONString();
        long startTime = record.getLongValue("startTime") * 1000;
        long endTime = record.getLongValue("endTime") * 1000;
        String shortGameCode = record.getString("gameCode");
        Integer kindId = record.getInteger("kindId");
        Integer serverId = record.getInteger("serverId");
        Map<String, Object> gameRoomInfo = platformServiceClient.getServerName(serverId);
        for (Object d : detailList) {
            JSONObject dJson = JSONObject.parseObject(d.toString());
            boolean isRobot = dJson.getBooleanValue("isRobot");
            if (isRobot) {
                //水浒传将机器人数据存入幸运玩家表中
                if (kindId.equals(235)) {
                    AccountsInfoVO accountsInfo = this.accountsServiceClient.getUserInfoByGameId(dJson.getInteger("gameId"));
                    LuckyVO luckyVO = new LuckyVO();
                    luckyVO.setScore(dJson.getBigDecimal("score").setScale(2, BigDecimal.ROUND_HALF_DOWN));
                    luckyVO.setEndTime(endTime);
                    luckyVO.setServerId(serverId);
                    luckyVO.setNickName(accountsInfo.getAccount());
                    mongoTemplate.save(luckyVO, "Lucky");
                }
                continue;
            }
            GameRecord gr = new GameRecord();
            if (Integer.parseInt(gameRoomInfo.get("ServerType").toString()) == 16) {
                gr.setGamePersonal(record.getJSONObject("game_personal").toJSONString());
            }
            Integer gameId = dJson.getInteger("gameId");
            gr.setPlayerId(gameId);
            gr.setServerId(serverId);
            gr.setKindId(kindId);
            gr.setGameCode(shortGameCode + "-" + dJson.getString("chairId"));
            gr.setStartTime(startTime);
            gr.setEndTime(endTime);
            gr.setGameName(gameRoomInfo.get("ServerName").toString());
            gr.setScore(dJson.getBigDecimal("score"));
            gr.setRevenue(dJson.getBigDecimal("revenue"));
            if (gr.getScore() == null) {
                gr.setScore(BigDecimal.ZERO);
            }
            if (gr.getRevenue() == null) {
                gr.setRevenue(BigDecimal.ZERO);
            }
            String key = RedisKeyPrefix.getKey(gameId.toString());
            gr.setBetAmount(gr.getScore().add(gr.getRevenue()));
            AccountsInfoVO accountsInfo = null;
            accountsInfo = redisDao.get(key, AccountsInfoVO.class);
            if (accountsInfo != null) {
                //设置账户信息
                accountsInfos(gr, accountsInfo);
            } else {
                //设置账户信息
                accountsInfo = this.accountsServiceClient.getUserInfoByGameId(gameId);
                redisDao.set(key, accountsInfo);
                redisDao.expire(key, 60, TimeUnit.MINUTES);
                accountsInfos(gr, accountsInfo);
            }
            gr.setPersonalDetails(String.valueOf(dJson));
            gr.setDetail(detailString);
            //获取相对应游戏数据库表名
            String tableName = StringUtils.substringBeforeLast(StringUtils.substringBeforeLast(accountsServiceClient.getGameItem(gr.getKindId()), "Server"), "_");
            mongoTemplate.save(gr, "gameRecord_" + tableName);
            mongoTemplate.save(gr);
            if (kindId.equals(235)) {
                if (dJson.getBooleanValue("lucky")) {
                    //将玩家数据存入幸运玩家表中
                    LuckyVO luckyVO = new LuckyVO();
                    luckyVO.setScore(dJson.getBigDecimal("score").setScale(2, BigDecimal.ROUND_HALF_DOWN));
                    luckyVO.setEndTime(endTime);
                    luckyVO.setServerId(serverId);
                    luckyVO.setNickName(accountsInfo.getAccount());
                    mongoTemplate.save(luckyVO, "Lucky");
                }
            }
        }
        return globeResponse;
    }

    //设置账户信息
    private void accountsInfos(GameRecord gr, AccountsInfoVO accountsInfo) {
        gr.setAccount(accountsInfo.getAccount());
        gr.setH5Account(accountsInfo.getH5Account());
        gr.setH5SiteCode(accountsInfo.getH5siteCode());
    }

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
            params.put("text", "【王者科技】您的验证码是" + vCode + "。如非本人操作，请忽略本短信");
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
//    @Deprecated
//    @RequestMapping("/payPageLoad")
//    private String payPageLoad(int userId, String account, BigDecimal amount, int qudaoId, HttpServletRequest request) throws YunpianException {
//        if (amount.equals(null) || qudaoId <= 0 || userId <= 0 || StringUtil.isEmpty(account)) {
//            throw new GlobeException(SystemConstants.FAIL_CODE, "参数错误!");
//        }
//        StringBuffer url = request.getRequestURL();
//        String tempContextUrl = url.delete(url.length() - request.getRequestURI().length(), url.length()).toString();
//        StringBuilder html = new StringBuilder();
//        html.append("<!DOCTYPE html><html><head><title>支付页面</title><style>.tabPages{margin-top:150px;text-align:center;display:block; border:3px solid #d9d9de; padding:30px; font-size:14px;");
//        html.append("}</style></head><body>");
//        html.append("<div id=\"Content\"><div class=\"tabPages\">我们正在为您连接银行，请稍等......</div></div><form name=\"sendForm\" action=\"" + tempContextUrl + "/mobileInterface/payPageLoad/submit\" method=\"post\">");
//        html.append("<input type=\"hidden\" name=\"userId\"  value=\"" + userId + "\"><input type=\"hidden\" name=\"account\"  value=\"" + account + "\">");
//        html.append("<input type=\"hidden\" name=\"qudaoId\"  value=\"" + qudaoId + "\"><input type=\"hidden\" name=\"amount\"  value=\"" + amount + "\">");
//        html.append("</form><script>document.sendForm.submit();</script></body></html>");
//        return html.toString();
//    }

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
//    	String key = RedisKeyPrefix.getPayPageLoadSubmitLockKey(userId);
//    	String lock = redisDao.get(key, String.class);
//    	if(!StringUtil.isEmpty(lock)) {
//        	throw new GlobeException(SystemConstants.FAIL_CODE, "正在连接银行，请稍等...");
//	    }
//    	redisDao.set(key, "lock");
//    	redisDao.expire(key, 3, TimeUnit.SECONDS);
        ViewPayInfoVO payInfoVO = treasureServiceClient.getPayInfo(qudaoId);
        TpayOwnerInfoVO payOwnerInfo = treasureServiceClient.getPayOwnerInfo();

        OnLineOrderVO onLineOrderVO = new OnLineOrderVO();

        onLineOrderVO.setOrderId(GetOrderIDByPrefix("e", userId));  //订单标识 时间+userId

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
            String sendUrl = PayLineCheckJob.PAY_LINE + payInfoVO.getSendUrl();
            log.info("发送到中转中心：" + sendUrl + "?" + params);
            mag = HttpRequest.sendPost(sendUrl, params);
            log.info("中转中心返回：userId={},amount={},qudaoId={}, 内容：{}", userId, amount, qudaoId, mag);
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
        Object insureScore = data.get("insureScore");
        Object level = data.get("vipLevel");
        String msg = "{\"msgid\":7,\"userId\":" + userId + ", \"score\":" + score + ",\"insuranceScore\":" + insureScore +
                ", \"VipLevel\":" + level + ", \"type\":" + 1 + ", \"Charge\":" + amount + "}";
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
    public String updateMerchantOrderId(String ownerOrderId, String merchantOrderId, Integer orderStatus) {
        if (orderStatus == null) {
            orderStatus = 0;
        }
        this.treasureServiceClient.updateMerchantOrderId(ownerOrderId, merchantOrderId, orderStatus);
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
    private GlobeResponse<Object> getMinBalanceInfo(Integer agentId, Integer userId) {
        long startMillis = System.currentTimeMillis();
        log.info("/getMinBalanceInfo,参数agentId={}", agentId);
        if (agentId == null) {
            throw new GlobeException(SystemConstants.FAIL_CODE, "参数错误");
        }
        BigDecimal minBalance = accountsServiceClient.getMinBalanceInfo(agentId, userId);
        Map<String, Object> data = new HashMap<>();
        data.put("minBalance", minBalance);  //最低出售金币
//        data.put("counterFee", goldExchangeVO.getCounterFee() * 100);   //支付宝提现手续费
//        data.put("minCounterFee", goldExchangeVO.getMinCounterFee());   //支付宝提现最低手续费
//        data.put("bankCounterFee", goldExchangeVO.getBankCounterFee().multiply(new BigDecimal(100)));   //银行卡提现最低手续费
//        data.put("minBankCounterFee", goldExchangeVO.getMinBankCounterFee());   //银行卡提现最低手续费
//        data.put("isOpenAli", goldExchangeVO.getIsOpenAli());    //是否开启支付宝 0 开启  1是禁用
//        data.put("IsOpenBank", goldExchangeVO.getIsOpenBank()); //是否开启银行卡  0 开启  1是禁用
        GlobeResponse<Object> globeResponse = new GlobeResponse<>();
        globeResponse.setData(data);
        log.info("/getMinBalanceInfo,耗时:{}", System.currentTimeMillis() - startMillis);
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
        List<BankInfoVO> bankInfos = platformServiceClient.getBankList(agentId);
        List<BankCardTypeVO> customers = new ArrayList<>();
        for (BankInfoVO b : bankInfos) {
            BankCardTypeVO bankCardTypeVO = new BankCardTypeVO();
            bankCardTypeVO.setBankCardType(b.getBankName());
            customers.add(bankCardTypeVO);
        }
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


    /**
     * 查询玩家VIP等级
     *
     * @param userId
     * @param parentId
     * @return
     */
    @RequestMapping("/getUserVipLevel")
    private GlobeResponse<Object> getUserVipLevel(Integer userId, Integer parentId) {
        if (userId == null) {
            throw new GlobeException(SystemConstants.FAIL_CODE, "参数错误");
        }
        Map<String, Object> data = new HashMap<>();
        GlobeResponse<Object> globeResponse = new GlobeResponse<>();
        Integer vipLevelCount = platformServiceClient.getVipLevelCount(parentId);
        List<VipRankReceiveVO> levelss = platformServiceClient.getUserLevelReceive(userId);
        //新创建用户添加VIP等级奖励
        if (levelss == null || levelss.size() == 0) {
            List<VipRankReceiveVO> lists = new ArrayList<>();
            for (int i = 1; i <= vipLevelCount; i++) {
                VipRankReceiveVO vo = new VipRankReceiveVO();
                vo.setVipRank(i);
                vo.setUserId(userId);
                vo.setNullity(false);
                vo.setRankMoney(BigDecimal.ZERO);
                vo.setReceiveDate(TimeUtil.getNow());
                lists.add(vo);

            }
            platformServiceClient.insertVipRankReceive(lists);
            try {
                Thread.sleep(1500);
            } catch (InterruptedException e) {
            }

        } else if (levelss.size() < vipLevelCount) {

            List<VipRankReceiveVO> lists = new ArrayList<>();
            for (int i = levelss.size() + 1; i <= vipLevelCount; i++) {
                VipRankReceiveVO vo = new VipRankReceiveVO();
                vo.setVipRank(i);
                vo.setUserId(userId);
                vo.setNullity(false);
                vo.setRankMoney(BigDecimal.ZERO);
                vo.setReceiveDate(TimeUtil.getNow());
                lists.add(vo);

            }
            platformServiceClient.insertVipRankReceive(lists);
            try {
                Thread.sleep(1500);
            } catch (InterruptedException e) {
            }
        }

        List<VipLevelRewardVO> w1 = new ArrayList<VipLevelRewardVO>();
        List<VipLevelRewardVO> w2 = new ArrayList<VipLevelRewardVO>();
        List<VipLevelRewardVO> w3 = new ArrayList<VipLevelRewardVO>();
        List<VipLevelRewardVO> w4 = new ArrayList<VipLevelRewardVO>();
        List<VipLevelRewardVO> w5 = new ArrayList<VipLevelRewardVO>();
        List<VipLevelRewardVO> w6 = new ArrayList<VipLevelRewardVO>();
        VipLevelRewardVO vipLevel = accountsServiceClient.getUserVipLevel(userId);
        List<VipLevelRewardVO> vipConfig = accountsServiceClient.getVipLevelConfig(parentId);
        //用户VIP等级   当前VIP积分 还差下一级需要多少积分
        int size = vipConfig.size() - 1;
        for (int i = 0; i < vipConfig.size(); i++) {
            if (vipLevel.getVipIntegral().compareTo(vipConfig.get(size).getVipIntegral()) == 1) {
                vipLevel.setVipIntegral(new BigDecimal("0"));
                vipLevel.setTotal(new BigDecimal("0"));
                break;
            }
            if (i == size) {
                vipLevel.setVipIntegral(new BigDecimal("0"));
                vipLevel.setTotal(vipConfig.get(i).getVipIntegral());
                break;
            }
            if (vipLevel.getVipLevel() == 0) {
                vipLevel.setVipIntegral(vipConfig.get(i).getVipIntegral().subtract(vipLevel.getVipIntegral()));
                vipLevel.setTotal(vipConfig.get(i).getVipIntegral());
                break;
            }
            if (vipLevel.getVipLevel() == vipConfig.get(i).getVipLevel()) {
                vipLevel.setVipIntegral(vipConfig.get(i + 1).getVipIntegral().subtract(vipLevel.getVipIntegral()));
                vipLevel.setTotal(vipConfig.get(i + 1).getVipIntegral());
                break;
            }

        }

        List<VipLevelRewardVO> list = platformServiceClient.getUserVIPLevelReward(parentId);
        List<VIPReceiveInfoVO> week = platformServiceClient.getUserWeekReceive(userId, vipLevel.getVipLevel());
        List<VIPReceiveInfoVO> month = platformServiceClient.getUserMonthReceive(userId, vipLevel.getVipLevel());
        List<VIPReceiveInfoVO> day = platformServiceClient.getUserDayReceive(userId, vipLevel.getVipLevel());
        List<VIPReceiveInfoVO> year = platformServiceClient.getUserYearReceive(userId, vipLevel.getVipLevel());
        List<VipRankReceiveVO> levels = platformServiceClient.getUserLevelReceive(userId);
        int levelsLen = levels.size();
        int rewardListLen = list.size();
        int loopIndex = rewardListLen > levelsLen ? levelsLen : rewardListLen;
        for (int i = 0; i < loopIndex; i++) {
            VipLevelRewardVO jf = new VipLevelRewardVO();
            jf.setVipLevel(list.get(i).getVipLevel());
            jf.setVipIntegral(list.get(i).getVipIntegral());
            w6.add(jf);
        }
        levelsLen = levels.size();
        rewardListLen = list.size();
        loopIndex = rewardListLen > levelsLen ? levelsLen : rewardListLen;
        //当前用户是否可以领取天奖励
        for (int i = 0; i < loopIndex; i++) {
            VipLevelRewardVO vo = new VipLevelRewardVO();
            int status = 1;
            if (day == null || day.size() == 0) {
                if (vipLevel.getVipLevel() == list.get(i).getVipLevel() && list.get(i).getDayReward().compareTo(BigDecimal.ZERO) > 0) {
                    status = 0;
                }
            } else {
                if (vipLevel.getVipLevel() == list.get(i).getVipLevel()) {
                    status = 2;
                }
            }
            vo.setVipLevel(list.get(i).getVipLevel());
            vo.setDayReward(list.get(i).getDayReward());
            vo.setStatus(status);
            w4.add(vo);
            if (levelsLen != rewardListLen) {
                if (i == (levelsLen - 1) || i == (rewardListLen - 1)) {
                    break;
                }
            }
        }
        levelsLen = levels.size();
        rewardListLen = list.size();
        loopIndex = rewardListLen > levelsLen ? levelsLen : rewardListLen;
        //当前用户是否可以领取周奖励
        for (int i = 0; i < loopIndex; i++) {
            VipLevelRewardVO vo = new VipLevelRewardVO();
            int status = 1;
            if (week == null || week.size() == 0) {
                if (vipLevel.getVipLevel() == list.get(i).getVipLevel() && list.get(i).getWeekReward().compareTo(BigDecimal.ZERO) > 0) {
                    status = 0;
                }
            } else {
                if (vipLevel.getVipLevel() == list.get(i).getVipLevel()) {
                    status = 2;
                }
            }
            vo.setVipLevel(list.get(i).getVipLevel());
            vo.setWeekReward(list.get(i).getWeekReward());
            vo.setStatus(status);
            w1.add(vo);
            if (levelsLen != rewardListLen) {
                if (i == (levelsLen - 1) || i == (rewardListLen - 1)) {
                    break;
                }
            }
        }
        levelsLen = levels.size();
        rewardListLen = list.size();
        loopIndex = rewardListLen > levelsLen ? levelsLen : rewardListLen;
        //当前用户是否可以领取月奖励
        for (int i = 0; i < loopIndex; i++) {
            VipLevelRewardVO vo = new VipLevelRewardVO();
            int status = 1;
            if (month == null || month.size() == 0) {
                if (vipLevel.getVipLevel() == list.get(i).getVipLevel() && list.get(i).getMonthReward().compareTo(BigDecimal.ZERO) > 0) {
                    status = 0;
                }
            } else {
                if (vipLevel.getVipLevel() == list.get(i).getVipLevel()) {
                    status = 2;
                }
            }
            vo.setVipLevel(list.get(i).getVipLevel());
            vo.setMonthReward(list.get(i).getMonthReward());
            vo.setStatus(status);
            w2.add(vo);
            if (levelsLen != rewardListLen) {
                if (i == (levelsLen - 1) || i == (rewardListLen - 1)) {
                    break;
                }
            }
        }
        levelsLen = levels.size();
        rewardListLen = list.size();
        loopIndex = rewardListLen > levelsLen ? levelsLen : rewardListLen;
        //当前用户是否可以领取年奖励
        for (int i = 0; i < loopIndex; i++) {
            VipLevelRewardVO vo = new VipLevelRewardVO();
            int status = 1;
            if (year == null || year.size() == 0) {
                if (vipLevel.getVipLevel() == list.get(i).getVipLevel() && list.get(i).getYearReward().compareTo(BigDecimal.ZERO) > 0) {
                    status = 0;
                }
            } else {
                if (vipLevel.getVipLevel() == list.get(i).getVipLevel()) {
                    status = 2;
                }
            }
            vo.setVipLevel(list.get(i).getVipLevel());
            vo.setYearReward(list.get(i).getYearReward());
            vo.setStatus(status);
            w5.add(vo);
            if (levelsLen != rewardListLen) {
                if (i == (levelsLen - 1) || i == (rewardListLen - 1)) {
                    break;
                }
            }
        }

        levelsLen = levels.size();
        rewardListLen = list.size();
        loopIndex = rewardListLen > levelsLen ? levelsLen : rewardListLen;
        //当前用户是否可以领取等级奖励
        for (int i = 0; i < loopIndex; i++) {
            VipRankReceiveVO levelItem = levels.get(i);
            VipLevelRewardVO listItem = list.get(i);
            VipLevelRewardVO vo = new VipLevelRewardVO();
            int status = 1;
            if (vipLevel.getVipLevel() >= levelItem.getVipRank() && levelItem.getNullity() == false && listItem.getVipRankReward().compareTo(BigDecimal.ZERO) > 0) {
                status = 0;
            }
            if (vipLevel.getVipLevel() >= levelItem.getVipRank() && levelItem.getNullity() == true) {
                status = 2;
            }
            vo.setVipLevel(listItem.getVipLevel());
            vo.setVipRankReward(listItem.getVipRankReward());
            vo.setStatus(status);
            w3.add(vo);
            if (levelsLen != rewardListLen) {
                if (i == (levelsLen - 1) || i == (rewardListLen - 1)) {
                    break;
                }
            }
        }
        Integer agentId = parentId;
        List<CleanChipsConfigVO> ls = platformServiceClient.getCleanChipsConfig(agentId);
        List<VipLevelRewardVO> clearBetAmount = new ArrayList<VipLevelRewardVO>();
        levelsLen = ls.size();
        rewardListLen = list.size();
        loopIndex = rewardListLen > levelsLen ? levelsLen : rewardListLen;
        //洗码比例
        for (int i = 0; i < loopIndex; i++) {
            VipLevelRewardVO vo = new VipLevelRewardVO();
            if (vipLevel.getVipLevel() == 0) {
                if (ls.get(i).getVipLevel() == 1) {
                    vo.setVipLevel(0);
                    vo.setVipVersion(ls.get(i).getVipVersion());
                } else {
                    vo.setVipLevel(ls.get(i).getVipLevel());
                    vo.setVipVersion(ls.get(i).getVipVersion());
                }
            } else {
                vo.setVipLevel(ls.get(i).getVipLevel());
                vo.setVipVersion(ls.get(i).getVipVersion());
            }
            clearBetAmount.add(vo);
            if (levelsLen != rewardListLen) {
                if (i == (levelsLen - 1) || i == (rewardListLen - 1)) {
                    break;
                }
            }
        }

        List<VipLevelRewardVO> yebRate = new ArrayList<VipLevelRewardVO>();
        levelsLen = levels.size();
        rewardListLen = list.size();
        loopIndex = rewardListLen > levelsLen ? levelsLen : rewardListLen;
        //余额宝利率
        for (int i = 0; i < loopIndex; i++) {
            VipLevelRewardVO vo = new VipLevelRewardVO();
            if (vipLevel.getVipLevel() == 0) {
                if (list.get(i).getVipLevel() == 1) {
                    vo.setVipLevel(0);
                    vo.setYebRate(list.get(i).getYebRate());
                } else {
                    vo.setVipLevel(list.get(i).getVipLevel());
                    vo.setYebRate(list.get(i).getYebRate());
                }
            } else {
                vo.setVipLevel(list.get(i).getVipLevel());
                vo.setYebRate(list.get(i).getYebRate());
            }
            yebRate.add(vo);
        }
        //data.put("isOpen", "0");
        data.put("VipLevels", vipLevel);
        data.put("weekList", w1);
        data.put("dayList", w4);
        data.put("monthList", w2);
        data.put("yearList", w5);
        data.put("vipLevelList", w3);
        data.put("clearBetAmount", clearBetAmount);
        data.put("vipLevelCount", vipLevelCount);
        data.put("yebRate", yebRate);
        data.put("vipScore", w6);
        globeResponse.setData(data);
        return globeResponse;
    }

    /**
     * 获取用户个人信息
     *
     * @param userId
     * @return
     */
    @RequestMapping("/getVipUserInfo")
    private GlobeResponse<Object> getVipUserInfo(Integer userId) {
        if (userId == null) {
            throw new GlobeException(SystemConstants.FAIL_CODE, "参数错误");
        }
        UserInformationVO userInfo = accountsServiceClient.getUsersInfo(userId);
        GlobeResponse<Object> globeResponse = new GlobeResponse<>();
        globeResponse.setData(userInfo);
        return globeResponse;
    }

    /**
     * 修改用户个人信息
     *
     * @param nickName
     * @param gender
     * @param userId
     * @return
     */
    @RequestMapping("/updateUserBasicInfo")
    private GlobeResponse<Object> updateUserBasicInfo(String nickName, Integer gender, Integer userId) {
        if (userId == null) {
            throw new GlobeException(SystemConstants.FAIL_CODE, "参数错误");
        }
        int count = accountsServiceClient.updateUserBasicInfo(nickName, gender, userId);
        GlobeResponse<Object> globeResponse = new GlobeResponse<>();
        globeResponse.setData(count);
        return globeResponse;
    }

    /**
     * 修改用户个人信息
     *
     * @param mobilePhone
     * @param qq
     * @param eMail
     * @param userId
     * @return
     */
    @RequestMapping("/updateUserContactInfo")
    private GlobeResponse<Object> updateUserContactInfo(String mobilePhone, String qq, String eMail, Integer userId,String agentId) {
        if (userId == null) {
            throw new GlobeException(SystemConstants.FAIL_CODE, "参数错误");
        }
        //
//        Integer num= accountsServiceClient.queryRegisterMobile(mobilePhone,agentId);
//        if(num>0){
//            throw new GlobeException(SystemConstants.FAIL_CODE, "电话号码已被注册，请重新设置");
//        }
        int count = accountsServiceClient.updateUserContactInfo(mobilePhone, qq, eMail, userId);
        GlobeResponse<Object> globeResponse = new GlobeResponse<>();
        globeResponse.setData(count);
        return globeResponse;
    }


    /**
     * 获取所有平台
     *
     * @return
     */
    @RequestMapping("/getVideoType")
    private GlobeResponse<Object> getVideoType() {
        List<VideoTypeVO> list = treasureServiceClient.getVideoType();
        VideoTypeVO videoTypeVO = new VideoTypeVO();
        videoTypeVO.setKindId(10000);
        videoTypeVO.setKindName("天天棋牌");
        list.add(videoTypeVO);
        videoTypeVO = new VideoTypeVO();
        videoTypeVO.setKindId(1);
        videoTypeVO.setKindName("全部");
        list.add(videoTypeVO);
        Collections.sort(list, Comparator.comparing(VideoTypeVO::getKindId));
        Map<String, Object> data = new HashMap<>();
        data.put("list", list);
        GlobeResponse<Object> globeResponse = new GlobeResponse<>();
        globeResponse.setData(data);
        return globeResponse;
    }

    /**
     * 获取时间
     *
     * @return
     */
    @RequestMapping("/getDate")
    private GlobeResponse<Object> getDate() {
        List<Map<String, String>> data = new ArrayList<>();
        Map<String, String> map = new HashMap<String, String>();
        map.put("code", "0");
        map.put("name", "全部时间");
        data.add(map);
        map = new HashMap<String, String>();
        map.put("code", "1");
        map.put("name", "今天");
        data.add(map);
        map = new HashMap<String, String>();
        map.put("code", "2");
        map.put("name", "昨天");
        data.add(map);
        map = new HashMap<String, String>();
        map.put("code", "3");
        map.put("name", "一个月内");
        data.add(map);
        Map<String, Object> maps = new HashMap<String, Object>();
        maps.put("list", data);
        GlobeResponse<Object> globeResponse = new GlobeResponse<>();
        globeResponse.setData(maps);
        return globeResponse;
    }

    /**
     * 获取投注记录
     *
     * @param kindType
     * @param date
     * @param kindId
     * @param userId
     * @param pageIndex
     * @param pageSize
     * @return
     */
    @RequestMapping("/getChannelGameUserBetAndScore")
    private GlobeResponse<Object> getChannelGameUserBetAndScore(Integer kindType, Integer date, Integer kindId, Integer userId, Integer pageIndex, Integer pageSize) {
        if (userId == null) {
            throw new GlobeException(SystemConstants.FAIL_CODE, "参数错误");
        }
        CommonPageVO<ChannelGameUserBetAndScoreVO> list = accountsServiceClient.getChannelGameUserBetAndScore(kindType, date, kindId, userId, pageIndex, pageSize);
        Map<String, Object> data = new HashMap<>();
        data.put("list", list.getLists());
        data.put("total", list.getPageCount());
        GlobeResponse<Object> globeResponse = new GlobeResponse<>();
        globeResponse.setData(data);
        return globeResponse;
    }

    /**
     * 获取所有交易类型
     *
     * @return
     */
    @RequestMapping("/getTransactionType")
    private GlobeResponse<Object> getTransactionType() {
        List<TransactionTypeVO> list = treasureServiceClient.getTransactionType();
        Collections.sort(list, Comparator.comparing(TransactionTypeVO::getTypeId));
        Map<String, Object> data = new HashMap<>();
        data.put("list", list);
        GlobeResponse<Object> globeResponse = new GlobeResponse<>();
        globeResponse.setData(data);
        return globeResponse;
    }

    /**
     * 获取账户明细
     *
     * @param userId
     * @param typeId
     * @param date
     * @param pageSize
     * @param pageIndex
     * @return
     */
    @RequestMapping("/getAccountDetails")
    private GlobeResponse<Object> getAccountDetails(Integer userId, Integer typeId, Integer date, Integer pageSize, Integer pageIndex) {
    	 if (userId == null) {
             throw new GlobeException(SystemConstants.FAIL_CODE, "参数错误");
         }
         GlobeResponse<Object> globeResponse = new GlobeResponse<>();
         Map<String, Object> data = new HashMap<>();
         CommonPageVO<MemberRechargeVO> page = treasureServiceClient.getAccountDetails(userId, typeId, date, pageSize, pageIndex);
         List<MemberRechargeVO> l = page.getLists();
         List<MemberRechargeVO> temp = new ArrayList<MemberRechargeVO>();
         if (typeId.equals(10)) {
             for (int i = 0; i < l.size(); i++) {
                 MemberRechargeVO vo = new MemberRechargeVO();
                 if (!StringUtils.isBlank(l.get(i).getCollectNote())) {
                     vo.setTypeName(l.get(i).getCollectNote());
                 } else {
                     vo.setTypeName(l.get(i).getTypeName());
                 }
                 vo.setBalance(l.get(i).getBalance());
                 vo.setCollectDate(l.get(i).getCollectDate());
                 if (l.get(i).getPresentScore().signum() == -1) {
                     vo.setExpenditureScore(l.get(i).getPresentScore().abs());
                 } else {
                     vo.setPresentScore(l.get(i).getPresentScore());
                 }
                 temp.add(vo);
                 page.setLists(temp);
             }
         } else if (typeId.equals(8)) {
             if (page.getLists() != null && page.getLists().size() > 0) {
                 page.getLists().forEach(object -> {
                     if (!StringUtils.isBlank(object.getCollectNote())) {
                         object.setTypeName(object.getCollectNote());
                     }
                 });
             }
         }
         AccountChangeStatisticsVO list = treasureServiceClient.accountChangeStatistics(userId, date);
         data.put("list", page.getLists());
         data.put("total", page.getPageCount());
         data.put("count", list);
         globeResponse.setData(data);
         return globeResponse;
    }


    /**
     * 获取个人报表
     *
     * @param userId
     * @param kindType
     * @param date
     * @return
     */
    @RequestMapping("/getPersonalReport")
    private GlobeResponse<Object> getPersonalReport(Integer userId, Integer kindType, Integer date) {
        if (userId == null) {
            throw new GlobeException(SystemConstants.FAIL_CODE, "参数错误");
        }
        GlobeResponse<Object> globeResponse = new GlobeResponse<>();
        Map<String, Object> data = new HashMap<>();
        PersonalReportVO list = accountsServiceClient.getPersonalReport(kindType, date, userId);
        BigDecimal rebate = agentServiceClient.getUserRebate(kindType, userId, date);
        list.setBackwater(rebate);
        list.setTotalProfit(list.getScore().add(rebate));
        data.put("list", list);
        globeResponse.setData(data);
        return globeResponse;
    }

    /**
     * 验证密码
     *
     * @param
     * @return
     */
    @RequestMapping("/verifyPassword")
    public GlobeResponse verifyPassword(Integer userId, String password) {
        if (userId == null || password == null) {
            throw new GlobeException(SystemConstants.FAIL_CODE, "参数错误");
        }
        GlobeResponse globeResponse = new GlobeResponse();
        String mdPassword = MD5Encode(password, "utf-8").toLowerCase();
        String password1 = accountsServiceClient.verifyPassword(userId).toLowerCase();
        if (StringUtil.isEmpty(password1)) {
            globeResponse.setData(true);//代表后台以重置密码
            return globeResponse;
        }
        if (mdPassword.equals(password1)) {
            return globeResponse;
        }
        globeResponse.setCode("-1");
        globeResponse.setMsg("验证失败");
        return globeResponse;
    }

    /**
     * 旧版获取红包奖励
     *
     * @param userId
     * @param parentId
     * @return
     */
    @RequestMapping("/getRedEnvelopeReward")
    private GlobeResponse<Object> getRedEnvelopeReward(Integer userId, Integer parentId) {
        if (userId == null) {
            throw new GlobeException(SystemConstants.FAIL_CODE, "参数错误");
        }
        if (parentId == null) {
            throw new GlobeException(SystemConstants.FAIL_CODE, "参数错误");
        }
        GlobeResponse<Object> globeResponse = new GlobeResponse<>();
        Map<String, Object> data = new HashMap<>();
        List<ActivityRedEnvelopeRewardVO> list = accountsServiceClient.getRedEnvelopeReward(userId, parentId);
        data.put("list", list);
        globeResponse.setData(data);
        return globeResponse;
    }

    /**
     * 旧版领取红包奖励
     * @param userId
     * @param score
     * @param machineId
     * @param typeId
     * @param request
     * @return
     */
//    @RequestMapping("/getReceivingRedEnvelope")
//    private GlobeResponse<Object> getReceivingRedEnvelope(Integer userId,BigDecimal score,String machineId,Integer typeId,HttpServletRequest request) {
//    	if (userId == null) {
//            throw new GlobeException(SystemConstants.FAIL_CODE, "参数错误");
//        }
//    	GlobeResponse<Object> globeResponse = new GlobeResponse<>();
//    	Map<String, Object> data = new HashMap<>();
//    	String ip = getIpAddress(request);
//    	Integer ret = accountsServiceClient.getReceivingRedEnvelope(userId,score,ip,machineId,typeId);
//    	switch (ret) {
//		case -1:
//			throw new GlobeException(SystemConstants.FAIL_CODE, "抱歉，未知服务器错误!");
//		}
//    	data.put("code", ret);
//    	globeResponse.setData(data);
//    	return globeResponse;
//    }


    /**
     * 获取余额宝收益、余额宝日、月、年利率
     *
     * @param userId
     * @param parentId
     * @return
     */
    @RequestMapping("/getYebScore")
    private GlobeResponse<Object> getYebScore(Integer userId, Integer parentId) {
        if (userId == null) {
            throw new GlobeException(SystemConstants.FAIL_CODE, "参数错误");
        }
        GlobeResponse<Object> globeResponse = new GlobeResponse<>();
        Map<String, Object> data = new HashMap<>();
        YebScoreVO yebScore = treasureServiceClient.getYebScore(userId);
        if (yebScore == null) {
            yebScore = new YebScoreVO();
        }
        List<YebInterestRateVO> list = platformServiceClient.getYebInterestRate(parentId);
        data.put("yebScore", yebScore);
        data.put("yebInterestRate", list);
        globeResponse.setData(data);
        return globeResponse;
    }


    /**
     * 获取VIP奖励明细
     *
     * @param userId
     * @param parentId
     * @param pageSize
     * @param pageIndex
     * @return
     */
    @RequestMapping("/getUserRewardDetail")
    private GlobeResponse<Object> getUserRewardDetail(Integer userId, Integer parentId, Integer pageSize, Integer pageIndex) {
        if (userId == null) {
            throw new GlobeException(SystemConstants.FAIL_CODE, "参数错误");
        }
        GlobeResponse<Object> globeResponse = new GlobeResponse<>();
        Map<String, Object> data = new HashMap<>();
        CommonPageVO<UserRewardDetailVO> list = treasureServiceClient.getUserRewardDetail(userId, parentId, pageSize, pageIndex);
        data.put("VipRewardDetail", list.getLists());
        data.put("total", list.getPageCount());
        globeResponse.setData(data);
        return globeResponse;
    }


    /**
     * 获取余额宝明细
     *
     * @param userId
     * @param date
     * @param pageSize
     * @param pageIndex
     * @param typeId
     * @return
     */
    @RequestMapping("/getUserYebInfo")
    private GlobeResponse<Object> getUserYebInfo(Integer userId, Integer date, Integer pageSize, Integer pageIndex, Integer typeId) {
        if (userId == null || typeId == null || date == null) {
            throw new GlobeException(SystemConstants.FAIL_CODE, "参数错误");
        }
        GlobeResponse<Object> globeResponse = new GlobeResponse<>();
        Map<String, Object> data = new HashMap<>();
        globeResponse.setData(data);
        CommonPageVO<UserRecordInsureVO> list;
        if (typeId > 0) {
            list = treasureServiceClient.getUserRecordInsure(userId, date, pageSize, pageIndex, typeId);
        } else {
            list = treasureServiceClient.getUserYebIncome(userId, date, pageSize, pageIndex);
        }
        if (list != null) {
            data.put("yebAccountChange", list.getLists());
            data.put("total", list.getPageCount());
        }
        return globeResponse;
    }

    @RequestMapping("/getActivityType")
    public GlobeResponse<Object> getActivityType() {
        GlobeResponse<Object> globeResponse = new GlobeResponse<>();
        globeResponse.setData(nativeWebServiceClient.getActivityType());
        return globeResponse;
    }

    @RequestMapping("/getActivityListByMobile")
    public GlobeResponse<Object> getActivityListByMobile(Integer gameCategory, Integer agentId, Integer pageIndex) {
        GlobeResponse<Object> globeResponse = new GlobeResponse<>();
        if (agentId == null) {
            throw new GlobeException(SystemConstants.FAIL_CODE, "参数错误!");
        }
        if (gameCategory == null) {
            throw new GlobeException(SystemConstants.FAIL_CODE, "参数错误!");
        }
        if (pageIndex == null) {
            pageIndex = 1;
        }
        CommonPageVO page = nativeWebServiceClient.getActivityListByMobile(agentId, gameCategory, pageIndex);
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("list", page.getLists());
        data.put("total", page.getPageCount());
        globeResponse.setData(data);
        return globeResponse;
    }

    /**
     * 红包雨
     *
     * @param parentId
     * @param userId
     * @return
     */
    @RequestMapping("/getRedEnvelopeRain")
    public GlobeResponse<Object> getRedEnvelopeRain(Integer parentId, Integer userId) {
        GlobeResponse<Object> globeResponse = new GlobeResponse<>();
        RedEnvelopeVO v = agentServiceClient.getRedEnvelope(parentId);
        if (v != null) {
            int count = agentServiceClient.userSingleRedEnvelopeCount(userId, parentId, v.getEventId());
            if (count < 1) {
                count = agentServiceClient.todayRedEnvelopeRainCount(v.getEventId(), parentId);
                if (count < v.getLimitedNumber()) {
                    HashMap<String, Object> data = new HashMap<>();
                    data.put("id", v.getEventId());
                    data.put("redAmount", 0.01);
                    globeResponse.setData(data);
                    return globeResponse;
                }
            }
        }
        globeResponse.setData("");
        return globeResponse;
    }

    /**
     * 领取红包雨红包
     *
     * @param request
     * @param id
     * @param userId
     * @param machineId
     * @return
     */
    @RequestMapping("/receiveRedEnvelopeRain")
    public GlobeResponse<Object> receiveRedEnvelopeRain(HttpServletRequest request, Integer id, Integer userId, String machineId) {
        GlobeResponse<Object> globeResponse = new GlobeResponse<>();
        String ip = getIpAddress(request);
        Map<String, Object> param = this.agentServiceClient.receiveRedEnvelopeRain(id, userId, machineId, ip);
        if (param == null) {
            throw new GlobeException(SystemConstants.EXCEPTION_CODE, "领取失败-!");
        }
        Integer ret = (Integer) param.get("ret");
        String msg = param.get("msg").toString();
        if (ret.intValue() == 0) {
            globeResponse.setMsg("领取成功");
            globeResponse.setData(param.get("money"));
            return globeResponse;
        }
        throw new GlobeException(SystemConstants.FAIL_CODE, msg);
    }

    /**
     * 提现信息审核开关
     */
    @RequestMapping("/getIndividualDatumStatus")
    public GlobeResponse<Object> getIndividualDatumStatus(Integer agentId, Integer gameId) throws ParseException {
        GlobeResponse<Object> globeResponse = new GlobeResponse<>();
        if (agentId == null || agentId == 0) {
            throw new GlobeException(SystemConstants.FAIL_CODE, "参数错误!");
        }
        if (gameId == null || gameId == 0) {
            throw new GlobeException(SystemConstants.FAIL_CODE, "参数错误!");
        }
        IndividualDatumVO pageVO = treasureServiceClient.getIndividualDatum(agentId, gameId);
        if (pageVO == null) {
            globeResponse.setData(new IndividualDatumVO());//此用户未曾绑定银行卡
            return globeResponse;
        } else {
            if (StringUtils.isBlank(pageVO.getBankNO())) {
                globeResponse.setData(new IndividualDatumVO());//此用户未曾绑定银行卡
                return globeResponse;
            }
            if (!StringUtils.isBlank(pageVO.getCollectDate()) && (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(pageVO.getCollectDate()).getTime()) < (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2020-01-04 08:00:00").getTime())) {
                pageVO.setStatus(4);//兼容历史数据（绑定成功）
            }
            globeResponse.setData(pageVO);
            return globeResponse;
        }
        /*Boolean flag = this.treasureServiceClient.getIndividualDatumStatus(agentId,gameId);
        if (flag) {
            globeResponse.setData(pageVO);
            return globeResponse;
        }
        globeResponse.setData(flag);
        return globeResponse;*/
    }

    /* 新版获取红包
     * @param userId
     * @param parentId
     * @return
     */
    @RequestMapping("/getRedEnvelope")
    public GlobeResponse<Object> getRedEnvelope(Integer userId, Integer parentId) {
        if (userId == null) {
            throw new GlobeException(SystemConstants.FAIL_CODE, "参数错误");
        }
        if (parentId == null) {
            throw new GlobeException(SystemConstants.FAIL_CODE, "参数错误");
        }
    	GlobeResponse<Object> globeResponse = new GlobeResponse<>();
    	Map<String, Object> data = new HashMap<>();
    	List<ActivityRedEnvelopeVO> list = accountsServiceClient.getRedEnvelope(userId,parentId);//获取取每日充值、累计充值、每日打码、累计打码 红包
    	Integer sf = accountsServiceClient.getUserLoginRedEnvelope(parentId);   //查询业主是否配置登录红包
    	Integer hby = accountsServiceClient.getUserRedEnvelopeRain(parentId);   //查询业主是否配置红包雨
    	RedEnvelopeVO v = agentServiceClient.getRedEnvelopeSain(parentId);   //是否有红包雨活动
    	RedEnvepoleYuStartTimeAndEndTimeVO redVO = new RedEnvepoleYuStartTimeAndEndTimeVO();
    	Long currentTime = agentServiceClient.getCurrentDate();
    	if(hby > 0) {
    		if(v != null) {
    			redVO = agentServiceClient.getRedEnvepoleYuStartTimeAndEndTime(parentId, v.getEventId());
        		redVO.setStatus(0);		//当天有红包雨活动 开始倒计时
        		redVO.setDayStartTime(redVO.getDayStartTime() * 1000);
        		redVO.setDayEndTime(redVO.getDayEndTime() * 1000);
        		redVO.setCurrentTime(currentTime * 1000);
        		redVO.setActivityId(v.getEventId());
    		}
    		if(v == null) {
    			v = agentServiceClient.getTomorrowRedEnvelopeSain(parentId);
    			if(v !=null) {
    				redVO.setStatus(0);		//获取第二天红包雨
            		redVO.setDayStartTime(v.getDayStartTime() * 1000);
            		redVO.setDayEndTime(v.getDayEndTime() * 1000);
            		redVO.setCurrentTime(currentTime * 1000);
            		redVO.setActivityId(v.getEventId());
    			}
    		}
    		if(v != null) {
        		RedEnvelopeVO v1 = agentServiceClient.getRedEnvelope(parentId);
            	if(v1 != null) {
            		int count1 = agentServiceClient.userSingleRedEnvelopeCount(userId, parentId, v1.getEventId());
            		if(count1 < 1) {
            			count1 = agentServiceClient.todayRedEnvelopeRainCount(v1.getEventId(), parentId);
            			if(count1 < v1.getLimitedNumber()) {
            				redVO.setRedAmount(1);   //客户端十分钟请求一次  如果金额大于0  APP右上角红包抖动
            				redVO.setStatus(2);      //红包雨可领取状态
            				redVO.setActivityId(v1.getEventId());
            				redVO.setCurrentTime(currentTime * 1000);
            			}
            		}
            	}else {
            		redVO.setRedAmount(0);
            	}

        	}else {
        		redVO.setStatus(1);   //活动已结束
        	}
    	}else {
    		redVO.setStatus(4);    //状态为4  客户端不展示红包雨
    	}

        LoginRedEnvepoleStatusVO vo = new LoginRedEnvepoleStatusVO();
        if (sf > 0) {
            Integer isOpen = accountsServiceClient.getUserLoginRedEnvelopeIsOpen(parentId);    //获取登录红包状态
            Integer count = accountsServiceClient.getReceiveRedEnvelopeRecord(userId);  //查询用户是否领取过登录红包
            if (isOpen > 0) {
                if (count > 0) {
                    vo.setLoginRedEnvepoleStatus(1);    //已领取
                } else {
                    BigDecimal amount = accountsServiceClient.getLoginRedEnvelopeAmoutByParentId(parentId);
                    vo.setLoginRedEnvepoleStatus(0);  //可领取
                    vo.setRedAmount(amount);        //登录红包金额
                }
            } else {
                vo.setLoginRedEnvepoleStatus(2);    //活动已结束
            }
        } else {
            vo.setLoginRedEnvepoleStatus(4);    //状态为4  客户端不展示登录红包
        }
        List<LoginRedEnvepoleStatusVO> l = new ArrayList<LoginRedEnvepoleStatusVO>();
        l.add(vo);
        List<RedEnvepoleYuStartTimeAndEndTimeVO> le = new ArrayList<RedEnvepoleYuStartTimeAndEndTimeVO>();
        le.add(redVO);
        data.put("OthersRedEnvepoleList", list);  //每日充值红包 累计充值红包  每日打码量红包  累计打码量红包  在这个这里
        data.put("LoginRedEnvepoleStatus", l);  //登录红包  这个只是获取登录红包是否已被领取
        data.put("RedEnvelopeRain", le);        //红包雨
        globeResponse.setData(data);
        return globeResponse;
    }


    /**
     * 新版领取红包奖励
     *
     * @param userId
     * @param score
     * @param machineId
     * @param typeId
     * @param request
     * @return
     */
    @RequestMapping("/getReceivingRedEnvelopes")
    private GlobeResponse<Object> getReceivingRedEnvelopes(Integer userId, BigDecimal score, String machineId, Integer typeId, Integer activityId, HttpServletRequest request) {
        if (userId == null) {
            throw new GlobeException(SystemConstants.FAIL_CODE, "参数错误");
        }
        GlobeResponse<Object> globeResponse = new GlobeResponse<>();
        Map<String, Object> data = new HashMap<>();
        String ip = getIpAddress(request);
        Integer ret = accountsServiceClient.getReceivingRedEnvelope(userId, score, ip, machineId, typeId, activityId);  //activityId
        switch (ret) {
            case -1:
                throw new GlobeException(SystemConstants.FAIL_CODE, "抱歉，未知服务器错误!");
        }
        data.put("code", ret);
        globeResponse.setData(data);
        return globeResponse;
    }

    /**
     * 获取红包手气榜
     *
     * @param userId
     * @param parentId
     * @return
     */
    @RequestMapping("/getUserRankings")
    public GlobeResponse<Object> getUserRankings(Integer parentId) {
        if (parentId == null) {
            throw new GlobeException(SystemConstants.FAIL_CODE, "参数错误");
        }
        GlobeResponse<Object> globeResponse = new GlobeResponse<>();
        Map<String, Object> data = new HashMap<>();
        List<UserRankinsVO> list = agentServiceClient.getUserRankings(parentId);
        data.put("list", list);
        globeResponse.setData(data);
        return globeResponse;
    }


    /**
     * 获取红包类型
     *
     * @return
     */
    @RequestMapping("/getRedEnvelopeType")
    public GlobeResponse<Object> getRedEnvelopeType() {
        GlobeResponse<Object> globeResponse = new GlobeResponse<>();
        Map<String, Object> data = new HashMap<>();
        List<RedEnvelopeConditionTypeVO> list = agentServiceClient.getRedEnvelopeType();
        RedEnvelopeConditionTypeVO vo = new RedEnvelopeConditionTypeVO();
        vo.setTypeId(0);
        vo.setTypeName("全部");
        list.add(vo);
        Collections.sort(list, Comparator.comparing(RedEnvelopeConditionTypeVO::getTypeId));
        data.put("list", list);
        globeResponse.setData(data);
        return globeResponse;
    }


    /**
     * 获取时间
     *
     * @return
     */
    @RequestMapping("/getDateTime")
    private GlobeResponse<Object> getDateTime() {
        List<Map<String, String>> data = new ArrayList<>();
        Map<String, String> map = new HashMap<String, String>();
//        map.put("code", "0");
//        map.put("name", "全部时间");
//        data.add(map);
        map = new HashMap<String, String>();
        map.put("code", "1");
        map.put("name", "今天");
        data.add(map);
        map = new HashMap<String, String>();
        map.put("code", "2");
        map.put("name", "最近一周");
        data.add(map);
        map = new HashMap<String, String>();
        map.put("code", "3");
        map.put("name", "最近一个月");
        data.add(map);
        map = new HashMap<String, String>();
        map.put("code", "4");
        map.put("name", "最近三个月");
        data.add(map);
        Map<String, Object> maps = new HashMap<String, Object>();
        maps.put("list", data);
        GlobeResponse<Object> globeResponse = new GlobeResponse<>();
        globeResponse.setData(maps);
        return globeResponse;
    }

    /**
     * 红包记录
     *
     * @param userId
     * @param typeId
     * @param pageIndex
     * @param pageSize
     * @return
     */
    @RequestMapping("/getRedEnvelopeRecord")
    public GlobeResponse<Object> getRedEnvelopeRecord(Integer userId, Integer typeId, Integer pageIndex, Integer pageSize, Integer date) {
        GlobeResponse<Object> globeResponse = new GlobeResponse<>();
        Map<String, Object> data = new HashMap<>();
        CommonPageVO<RedEnvelopeRecordVO> list = agentServiceClient.getRedEnvelopeRecord(userId, typeId, pageIndex, pageSize, date);
        if (list != null) {
            data.put("list", list.getLists());
            data.put("total", list.getPageCount());
        }
        globeResponse.setData(data);
        return globeResponse;
    }

    /**
     * 活动规则
     *
     * @param parentId
     * @return
     */
    @RequestMapping("/getRedEnvepoleRules")
    public GlobeResponse<Object> getRedEnvepoleRules(Integer parentId) {
        GlobeResponse<Object> globeResponse = new GlobeResponse<>();
        RedEnvepoleRulesVO vo = accountsServiceClient.getRedEnvepoleRules(parentId);
        Map<String, Object> data = new HashMap<>();
        data.put("rules", vo);
        globeResponse.setData(data);
        return globeResponse;
    }

    @RequestMapping("/getNoticeTitile")
    private GlobeResponse<List<NewsVO>> getNoticeTitile(Integer agentId) {
        if (agentId == null) {
            throw new GlobeException(SystemConstants.FAIL_CODE, "参数错误");
        }
        GlobeResponse<List<NewsVO>> globeResponse = new GlobeResponse<>();
        globeResponse.setData(nativeWebServiceClient.getNoticeTitile(1, agentId));
        return globeResponse;
    }

    @RequestMapping("/getNoticeDetail")
    private GlobeResponse<String> getNoticeDetail(Integer newsId) {
        if (newsId == null) {
            throw new GlobeException(SystemConstants.FAIL_CODE, "参数错误");
        }
        GlobeResponse<String> globeResponse = new GlobeResponse<String>();
        globeResponse.setData(nativeWebServiceClient.getNoticeDetail(newsId));
        return globeResponse;
    }

    /**
     * 绑定银行卡
     * @param userId
     * @param gameId
     * @param agentId
     * @param bankNo
     * @param bankName
     * @param compellation
     * @param bankAddress
     * @return
     */
    @RequestMapping("/saveBankCardRawData")
    private GlobeResponse<String> saveBankCardRawData(Integer userId, Integer gameId, Integer agentId, String bankNo, String bankName, String compellation, String bankAddress) {
        if (userId == null || agentId == null || StringUtils.isBlank(bankNo) || StringUtils.isBlank(bankName)) {
            throw new GlobeException(SystemConstants.FAIL_CODE, "参数错误");
        }
        GlobeResponse<String> globeResponse = new GlobeResponse<String>();
        Boolean flag = accountsServiceClient.saveBankCardRawData(userId, gameId, agentId, bankNo, bankName, compellation, bankAddress);
        if (!flag) {
            throw new GlobeException(SystemConstants.FAIL_CODE, "保存成功,如果问题,请联系客服");
        }
        return globeResponse;
    }

    @RequestMapping("/getBankCardRawData")
    private GlobeResponse<IndividualDatumVO> getBankCardRawData(Integer gameId, Integer agentId) {
        if (gameId == null || agentId == null) {
            throw new GlobeException(SystemConstants.FAIL_CODE, "参数错误");
        }
        GlobeResponse<IndividualDatumVO> globeResponse = new GlobeResponse<IndividualDatumVO>();
        globeResponse.setData(accountsServiceClient.getBankCardRawData(gameId, agentId));
        return globeResponse;
    }

    /**
     * 余额宝说明查询
     */
    @RequestMapping("/getYuebaoDescription")
    public GlobeResponse<YebDescriptionVO> getYuebaoDescription(Integer agentId) {
        if (agentId == null) {
            throw new GlobeException(SystemConstants.FAIL_CODE, "参数错误");
        }
        YebDescriptionVO list = nativeWebServiceClient.getYuebaoDescription(agentId);
        GlobeResponse<YebDescriptionVO> globeResponse = new GlobeResponse<>();
        globeResponse.setData(list);
        return globeResponse;
    }

    @RequestMapping("/getShareUrl")
    private GlobeResponse<String> getShareUrl(Integer g, Integer p) {
        if (g == null || p == null) {
            throw new GlobeException(SystemConstants.FAIL_CODE, "参数错误");
        }
        GlobeResponse<String> globeResponse = new GlobeResponse<String>();
        String shareJumpLinkKey = RedisKeyPrefix.shareJumpLinkKey();
        String shareUrl = redisDao.get(shareJumpLinkKey, String.class);
        if (StringUtil.isEmpty(shareUrl)) {
            globeResponse.setData("");
        } else {
            String shortParam = ShortUrlGenerator.ShortText("g=" + g + "&p=" + p);
            JSONObject j = new JSONObject();
            j.put("p", p);
            j.put("g", g);
            redisDao.set(RedisKeyPrefix.getShareParamKey(shortParam), j);
            globeResponse.setData(shareUrl + "/" + shortParam);
        }
        return globeResponse;
    }

    /**
     * 游戏公告
     *
     * @param agentId
     * @param pageIndex
     * @param pageSize
     * @return
     */
    @RequestMapping("/getGameNews")
    private GlobeResponse<List<SystemNewsVO>> getGameNews(Integer agentId, Integer pageIndex, Integer pageSize) {
        if (agentId == null) {
            throw new GlobeException(SystemConstants.FAIL_CODE, "参数错误");
        }
        GlobeResponse<List<SystemNewsVO>> globeResponse = new GlobeResponse<>();
        globeResponse.setData(accountsServiceClient.getGameNews(agentId, pageIndex, pageSize));
        return globeResponse;
    }

    // ----------------签到奖励 start--------------------

    @RequestMapping("/getSignAwardConfigList")
    public GlobeResponse<Map> getSignAwardConfigList(Integer agentId, Integer userId) {
        GlobeResponse<Map> globeResponse = new GlobeResponse<>();
        Map responseData = new HashMap(50);
        responseData.put("configList", platformServiceClient.getUserSignAwardConfigList(agentId, userId));
        responseData.put("serverTime", System.currentTimeMillis());
        globeResponse.setData(responseData);
        return globeResponse;
    }

    /**
     * 未领取福利统计
     * @param agentId
     * @param userId
     * @return
     */
    @RequestMapping("/getTaskinfoCount")
    public Integer getTaskinfoCount(Integer agentId, Integer userId){
        return  platformServiceClient.getTaskinfoCount(agentId,userId);
    }

    /**
     * 签到
     *
     * @param agentId
     * @param userId
     * @return
     */
    @RequestMapping("/acceptUserSignAward")
    public GlobeResponse<String> acceptUserSignAward(Integer agentId, Integer userId) {
        GlobeResponse<String> globeResponse = new GlobeResponse<String>();
        RedisLock redisLock = null;
        try {
            redisLock = new RedisLock(RedisKeyPrefix.payLock("userBalance_" + userId), redisTemplate, 15000);
            Boolean hasLock = redisLock.tryLock();
            if (!hasLock) {
                globeResponse.setCode(SystemConstants.FAIL_CODE);
                globeResponse.setMsg("操作失败：请求太频繁，请稍后重试");
                return globeResponse;
            }
            BigDecimal awardAmount = platformServiceClient.acceptUserSignAward(agentId, userId);
            if (awardAmount == null || awardAmount.compareTo(BigDecimal.ZERO) < 0) {
                globeResponse.setCode(SystemConstants.FAIL_CODE);
                globeResponse.setMsg("当前签到无奖励");
            } else if (awardAmount.compareTo(BigDecimal.ZERO) == 0) {
                globeResponse.setCode(SystemConstants.SUCCESS_CODE);
                globeResponse.setData(awardAmount.toString());
                globeResponse.setMsg("当前签到无奖励");
            } else {
                globeResponse.setCode(SystemConstants.SUCCESS_CODE);
                globeResponse.setData(awardAmount.toString());
                globeResponse.setMsg("保存成功");
            }
        } catch (Exception e) {
            globeResponse.setCode(SystemConstants.FAIL_CODE);
            globeResponse.setMsg(e.getMessage());
        } finally {
            if (redisLock != null) {
                redisLock.unlock();
            }
        }
        return globeResponse;
    }

    // ----------------签到奖励 end---------------------


}
