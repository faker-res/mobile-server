package com.lzkj.mobile.v2.service;

import com.lzkj.mobile.client.AccountsServiceClient;
import com.lzkj.mobile.client.AgentServiceClient;
import com.lzkj.mobile.client.PlatformServiceClient;
import com.lzkj.mobile.client.TreasureServiceClient;
import com.lzkj.mobile.config.AgentSystemEnum;
import com.lzkj.mobile.redis.JsonUtil;
import com.lzkj.mobile.redis.RedisDao;
import com.lzkj.mobile.redis.RedisKeyPrefix;
import com.lzkj.mobile.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 *    
 *  *  
 *  * @Project: mobile-server 
 *  * @Package: com.lzkj.mobile.v2.service 
 *  * @Description: TODO   
 *  * @Author:   horus   
 *  * @CreateDate:  2020/2/14 11:33  
 *  * @Version:   v1.0
 *  *    
 *  
 */
@Slf4j
@Service
public class SystemConfigService {

    @Resource
    private RedisDao redisService;
    @Resource
    private AgentServiceClient agentClient;
    @Resource
    private AccountsServiceClient accountsClient;
    @Resource
    private PlatformServiceClient platformServiceClient;
    @Resource
    private TreasureServiceClient treasureServiceClient;
    @Value("${gameUrlList}")
    private String gameUrlList;

    @Value("${channelGameUrl}")
    private String channelGameUrl;

    @Value("${gameImg.url}")
    private String gameImgUrl;

    @Value("${huodong.url}")
    private String huodongurl;


    public Map<String, Object> getNewLoginStatus(Integer agentId, String registerMachine) {
        long timeMillis = System.currentTimeMillis();
        String dataKey = RedisKeyPrefix.getloginStatusCacheKey(agentId, registerMachine);
        Map<String, Object> cacheData = redisService.get(dataKey, Map.class);
        if (cacheData != null) {
            log.info("newLoginStatus：agentId:" + agentId + "\t registerMachine:" + registerMachine + ", 从redis获取数据，耗时：" + (System.currentTimeMillis() - timeMillis));
            return cacheData;
        }
        //String redisKey = RedisKeyPrefix.getQrCodeKey(agentId);
        //获取后台代理配置
        String redisKey = RedisKeyPrefix.getQrCode(agentId);
        AgentAccVO agentAccVO = redisService.get(redisKey, AgentAccVO.class);
        if (agentAccVO == null) {
            agentAccVO = agentClient.getQrCode(agentId);
            redisService.set(redisKey, agentAccVO);
            redisService.expire(redisKey, 2, TimeUnit.HOURS);
        }

        //总控的维护
        String controllerKey = RedisKeyPrefix.getControllerKey();
        SystemStatusInfoVO systemStatusInfo = redisService.get(controllerKey, SystemStatusInfoVO.class);
        if (null == systemStatusInfo) {
            String key = "EnjoinLogon";
            systemStatusInfo = accountsClient.getSystemStatusInfo(key);
            redisService.set(controllerKey, systemStatusInfo);
            redisService.expire(controllerKey, 2, TimeUnit.HOURS);
        }
        Boolean flag = false;
        if (systemStatusInfo.getStatusValue().compareTo(BigDecimal.ZERO) != 0) {
            flag = true;
        }
        Map<String, Object> data = new HashMap<>();

        //获取业主配置
        redisKey = RedisKeyPrefix.getAgentSystemStatusInfoKey(agentId);
        List<AgentSystemStatusInfoVO> agentSystemList;
        List agentSystemMapList = redisService.get(redisKey, List.class);
        if (null == agentSystemMapList) {
            agentSystemList = agentClient.getBindMobileSendInfo(agentId);
            redisService.set(redisKey, agentSystemList);
            redisService.expire(redisKey, 2, TimeUnit.HOURS);
        } else {
            agentSystemList = new ArrayList<>();
            for (Object item : agentSystemMapList) {
                AgentSystemStatusInfoVO assi = JsonUtil.parseObject(JsonUtil.parseJsonString(item), AgentSystemStatusInfoVO.class);
                agentSystemList.add(assi);
            }
        }

        //获取首页弹窗链接

        String imgUrl = "";
        //nativeWebServiceClient.getShowImgUrl(agentId);
        data.put("QRcode", agentAccVO.getAgentUrl());
        data.put("VERSION_APK", agentAccVO.getAgentVersion());
        data.put("ClientUrl", agentAccVO.getClientUrl());
        data.put("prompt", agentAccVO.getPrompt());
        data.put("hotVersion", agentAccVO.getHotVersion());
        data.put("channelGameUrl", channelGameUrl);
        data.put("showbanner", imgUrl);
        data.put("guanwangUrl", agentAccVO.getPrimaryDomain());
        data.put("loadingUrl",agentAccVO.getLoadingUrl());
        String[] gameUrl = gameUrlList.split(",");
        data.put("gameUrlList", gameUrl);
        String[] huodong = huodongurl.split(",");
        data.put("huodongurl", huodong);
        data.put("domainFont", agentAccVO.getDomainFont());
        for (AgentSystemStatusInfoVO vo : agentSystemList) {
            //是否系统维护
            if (!flag) {
                if (vo.getStatusName().equals(AgentSystemEnum.EnjoinLogon.getName())) {
                    if (vo.getStatusValue().compareTo(BigDecimal.ZERO) == 0) {
                        flag = true;
                    }
                }
            }
            //绑定手机
            if (vo.getStatusName().equals(AgentSystemEnum.BindMobileSend.getName())) {
                data.put("bindMobileSend", vo.getStatusValue());
            }
            //注册填写真实姓名
            if (vo.getStatusName().equals(AgentSystemEnum.ShowRealName.getName())) {
                if (vo.getIsShow() == 0) {
                    data.put("ShowRealName", true);
                } else {
                    data.put("ShowRealName", false);
                }
            }
            //注册时银行卡开关
            if (vo.getStatusName().equals(AgentSystemEnum.BANKOPEN.getName())) {
                if (vo.getStatusValue().compareTo(BigDecimal.ZERO) == 0) {
                    data.put("BankOpen", true);
                } else {
                    data.put("BankOpen", false);
                }
            }
            //注册时手机号开关
            if (vo.getStatusName().equals(AgentSystemEnum.REGISTEREDPHONEOPEN.getName())) {
                if (vo.getStatusValue().compareTo(BigDecimal.ZERO) == 0) {
                    data.put("RegisteredPhoneOpen", true);
                } else {
                    data.put("RegisteredPhoneOpen", false);
                }
            }
            //注册帐号开关
            if (vo.getStatusName().equals(AgentSystemEnum.REGISTERACCOUNTOPEN.getName())) {
                if (vo.getStatusValue().compareTo(BigDecimal.ZERO) == 0) {
                    data.put("RegisterAccountOpen", true);
                } else {
                    data.put("RegisterAccountOpen", false);
                }
            }

            //注册界面赠送金币图标开关
            if (vo.getStatusName().equals(AgentSystemEnum.ZCJMZSJBTBOPEN.getName())) {
//                if (vo.getStatusValue().compareTo(BigDecimal.ZERO) == 0) {
//                    data.put("ZCJMZSJBTBOpen", true);
                data.put("goldGiftCount", vo.getGoldGiftCount());
                switch (vo.getOptionButton()) {
                    case 0:
                        data.put("optionButton", 0);//不选
                        break;
                    case 1:
                        data.put("optionButton", 1);//账号
                        break;
                    case 2:
                        data.put("optionButton", 2);//手机号
                        break;
                    case 3:
                        data.put("optionButton", 3);//双选
                        break;
                }
//                } else {
//                    data.put("ZCJMZSJBTBOpen", false);
//                }
            }

            //提现时输入余额宝密码开关
            if (vo.getStatusName().equals(AgentSystemEnum.TXYEBPASSWORDOPEN.getName())) {
                if (vo.getStatusValue().compareTo(BigDecimal.ZERO) == 0) {
                    data.put("TXYEBpasswordOpen", true);
                } else {
                    data.put("TXYEBpasswordOpen", false);
                }
            }

            //余额宝密码开关
            if (vo.getStatusName().equals(AgentSystemEnum.YEBPASSWORDOPEN.getName())) {
                if (vo.getStatusValue().compareTo(BigDecimal.ZERO) == 0) {
                    data.put("YEBpasswordOpen", true);
                } else {
                    data.put("YEBpasswordOpen", false);
                }
            }
            //如果总控没有维护,并且业主维护的时候
            if (vo.getStatusName().equals(AgentSystemEnum.VIPOpen.getName())) {
                if (vo.getStatusValue().compareTo(BigDecimal.ZERO) == 0) {
                    data.put("vip", true);
                } else {
                    data.put("vip", false);
                }
            }
            //邮件系统是否开启
            if (vo.getStatusName().equals(AgentSystemEnum.MailOpen.getName())) {
                if (vo.getStatusValue().compareTo(BigDecimal.ZERO) == 0) {
                    data.put("mail", true);
                } else {
                    data.put("mail", false);
                }
            }
            //签到
            if (vo.getStatusName().equals(AgentSystemEnum.SignOpen.getName())) {
                if (vo.getStatusValue().compareTo(BigDecimal.ZERO) == 0) {
                    data.put("signUp", true);
                } else {
                    data.put("signUp", false);
                }
            }
            //修改密码开关
            if (vo.getStatusName().equals(AgentSystemEnum.ResetPwd.getName())) {
                if (vo.getStatusValue().compareTo(BigDecimal.ZERO) == 0) {
                    data.put("canResetdhmm", true);
                } else {
                    data.put("canResetdhmm", false);
                }
            }
            //活动展示
            if (vo.getStatusName().equals(AgentSystemEnum.ActivityOpen.getName())) {
                if (vo.getStatusValue().compareTo(BigDecimal.ZERO) == 0) {
                    data.put("ActivityOpen", true);
                } else {
                    data.put("ActivityOpen", false);
                }
            }
            //提现展示
            if (vo.getStatusName().equals(AgentSystemEnum.ApplyOrderOpen.getName())) {
                if (vo.getStatusValue().compareTo(BigDecimal.ZERO) == 0) {
                    data.put("ApplyOrderOpen", true);
                } else {
                    data.put("ApplyOrderOpen", false);
                }
            }
            //余额宝是否开启
            if (vo.getStatusName().equals(AgentSystemEnum.YebOpen.getName())) {
                if (vo.getStatusValue().compareTo(BigDecimal.ZERO) == 0) {
                    data.put("yebiIsopen", true);
                } else {
                    data.put("yebiIsopen", false);
                }
            }
            if (vo.getStatusName().equals(AgentSystemEnum.WXDLOpen.getName())) {
                if (vo.getStatusValue().compareTo(BigDecimal.ZERO) == 0) {
                    data.put("wxdlopen", true);
                } else {
                    data.put("wxdlopen", false);
                }
            }
            if (vo.getStatusName().equals(AgentSystemEnum.SJZCOpen.getName())) {
                if (vo.getStatusValue().compareTo(BigDecimal.ZERO) == 0) {
                    data.put("sjzcopen", true);
                } else {
                    data.put("sjzcopen", false);
                }
            }

            //红包开关
            if (vo.getStatusName().equals(AgentSystemEnum.REDEVENLOPE.getName())) {
                if (vo.getStatusValue().compareTo(BigDecimal.ZERO) == 0) {
                    data.put("redEnvelope", true);
                } else {
                    data.put("redEnvelope", false);
                }
            }

            //竞价包开启时间
            if (vo.getStatusName().equals(AgentSystemEnum.BIDPACKAGEOPEN.getName())) {
                data.put("BidPackageOpen", vo.getStatusValue());
            }
        }
        //获取房间信息
        redisKey = RedisKeyPrefix.getMobileKindList();
        List<MobileKind> mobileKindList;
        List mobileKindMapList = redisService.get(redisKey, List.class);
        if (null == mobileKindMapList) {
            Integer typeId = 1;
            mobileKindList = platformServiceClient.getMobileKindList(typeId, Integer.valueOf(agentId));
            redisService.set(redisKey, mobileKindList);
            redisService.expire(redisKey, 2, TimeUnit.HOURS);
        } else {
            mobileKindList = new ArrayList<>();
            for (Object item : mobileKindMapList) {
                MobileKind mi = JsonUtil.parseObject(JsonUtil.parseJsonString(item), MobileKind.class);
                mobileKindList.add(mi);
            }
        }
        data.put("GameList", mobileKindList);
        data.put("imgUrl", gameImgUrl);
        List<CloudShieldConfigurationVO> cloudShieldConfigurationVOS;
        redisKey = RedisKeyPrefix.getCloudShieldConfigurationInfos(agentId);
        List cloudShieldConfigurationMapList = redisService.get(redisKey, List.class);
        if (null == cloudShieldConfigurationMapList) {
            cloudShieldConfigurationVOS = agentClient.getCloudShieldConfigurationInfos(agentId);
            redisService.set(redisKey, cloudShieldConfigurationVOS);
            redisService.expire(redisKey, 2, TimeUnit.HOURS);
        } else {
            cloudShieldConfigurationVOS = new ArrayList<>();
            for (Object item : cloudShieldConfigurationMapList) {
                CloudShieldConfigurationVO cs = JsonUtil.parseObject(JsonUtil.parseJsonString(item), CloudShieldConfigurationVO.class);
                cloudShieldConfigurationVOS.add(cs);
            }
        }
        data.put("CloudData", cloudShieldConfigurationVOS);

        //获取新运转盘开关
        redisKey = RedisKeyPrefix.getLuckyIsOpen(agentId);
        LuckyTurntableConfigurationVO luckyTurntableConfigurationVO = redisService.get(redisKey, LuckyTurntableConfigurationVO.class);
        if (luckyTurntableConfigurationVO == null) {
            luckyTurntableConfigurationVO = treasureServiceClient.getLuckyIsOpen(agentId);
            redisService.set(redisKey, luckyTurntableConfigurationVO);
            redisService.expire(redisKey, 2, TimeUnit.HOURS);
        }
        if (luckyTurntableConfigurationVO != null) {
            data.put("luckyWheel", luckyTurntableConfigurationVO.getMainSwitch());
        }

        //判断预更新热更开关開啓沒
        if ("0".equals(String.valueOf(agentAccVO.getStatus()))) {
            String[] update = agentAccVO.getUpdateAddress().split(",");
            data.put("HOT_UPDATE_URL", update);
        }
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
        //判断业主是否开启负盈利
        Integer rewardStatus = treasureServiceClient.getRewardStatus(agentId);
        data.put("rewardStatus",rewardStatus);

        data.put("Maitance", flag);
        redisService.set(dataKey, data);
        redisService.expire(dataKey, 5, TimeUnit.SECONDS);
        log.info("newLoginStatus：agentId:" + agentId + "\t registerMachine:" + registerMachine + "，耗时：" + (System.currentTimeMillis() - timeMillis));
        return data;
    }
}
