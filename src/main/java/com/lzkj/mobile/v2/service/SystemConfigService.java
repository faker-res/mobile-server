package com.lzkj.mobile.v2.service;

import com.alibaba.fastjson.JSONArray;
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
 *  * @Author:      
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
        //获取后台代理配置
        AgentAccVO agentAccVO = getAgentAcc(agentId);

        //设置部分属性值
        Map<String, Object> data = handleSwitch(agentId, registerMachine, agentAccVO);

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

        //判断预更新热更开关開啓沒
        if ("0".equals(String.valueOf(agentAccVO.getStatus()))) {
            String[] update = agentAccVO.getUpdateAddress().split(",");
            data.put("HOT_UPDATE_URL", update);
        }

        //获取房间信息
        List<MobileKind> mobileKindList = getMobileKind(agentId);
        data.put("GameList", mobileKindList);

        data.put("imgUrl", gameImgUrl);

        List<CloudShieldConfigurationVO> cloudShieldConfigurationVOS = getCloudShieldConfiguration(agentId);
        data.put("CloudData", cloudShieldConfigurationVOS);

        //获取幸运转盘开关
        LuckyTurntableConfigurationVO luckyTurntableConfigurationVO = getLuckyTurntableConfiguration(agentId);
        if (luckyTurntableConfigurationVO != null) {
            data.put("luckyWheel", luckyTurntableConfigurationVO.getMainSwitch());
        }

        //判断业主是否开启负盈利
        Integer rewardStatus = treasureServiceClient.getRewardStatus(agentId);
        data.put("rewardStatus",rewardStatus);

        redisService.set(dataKey, data, 5, TimeUnit.SECONDS);
        log.info("newLoginStatus：agentId:" + agentId + "\t registerMachine:" + registerMachine + "，耗时：" + (System.currentTimeMillis() - timeMillis));
        return data;
    }

    /**
     * 获取幸运转盘开关
     * @param agentId
     * @return
     */
    private LuckyTurntableConfigurationVO getLuckyTurntableConfiguration(Integer agentId) {
        String redisKey = RedisKeyPrefix.getLuckyIsOpen(agentId);
        LuckyTurntableConfigurationVO vo = redisService.get(redisKey, LuckyTurntableConfigurationVO.class);
        if (vo == null) {
            vo = treasureServiceClient.getLuckyIsOpen(agentId);
            if(vo != null){
                redisService.set(redisKey, vo, 2, TimeUnit.HOURS);
            }
        }
        return vo;
    }

    /**
     *
     * @param agentId
     * @return
     */
    private List<CloudShieldConfigurationVO> getCloudShieldConfiguration(Integer agentId) {
        List<CloudShieldConfigurationVO> list;
        String redisKey = RedisKeyPrefix.getCloudShieldConfigurationInfos(agentId);
        String value = redisService.get(redisKey);
        if (null == value) {
            list = agentClient.getCloudShieldConfigurationInfos(agentId);
            if(list != null && !list.isEmpty()){
                redisService.set(redisKey, list, 2, TimeUnit.HOURS);
            }
        } else {
            list = JSONArray.parseArray(value, CloudShieldConfigurationVO.class);
        }
        return list;
    }

    /**
     * 获取房间信息
     * @param agentId
     * @return
     */
    private List<MobileKind> getMobileKind(Integer agentId) {
        List<MobileKind> list;
        String redisKey = RedisKeyPrefix.getMobileKindList();
        String mobileKind = redisService.get(redisKey);
        if (null == mobileKind) {
            Integer typeId = 1;
            list = platformServiceClient.getMobileKindList(typeId, Integer.valueOf(agentId));
            if(list != null && !list.isEmpty()){
                redisService.set(redisKey, list, 2, TimeUnit.HOURS);
            }
        } else {
            list = JSONArray.parseArray(mobileKind, MobileKind.class);
        }
        return list;
    }

    /**
     * 获取业主的开关配置
     * @param agentId
     * @return
     */
    private List<AgentSystemStatusInfoVO> getAgentSystemStatusInfo(Integer agentId) {
        List<AgentSystemStatusInfoVO> list;
        String redisKey = RedisKeyPrefix.getAgentSystemStatusInfoKey(agentId);
        String agentSystem = redisService.get(redisKey);
        if (null == agentSystem) {
            list = agentClient.getBindMobileSendInfo(agentId);
            if(list != null && !list.isEmpty()){
                redisService.set(redisKey, list, 2, TimeUnit.HOURS);
            }
        } else {
            list = JSONArray.parseArray(agentSystem, AgentSystemStatusInfoVO.class);
        }
        return list;
    }

    /**
     * 获取后台代理配置
     * @param agentId
     * @return
     */
    private AgentAccVO getAgentAcc(Integer agentId){
        String redisKey = RedisKeyPrefix.getQrCode(agentId);
        AgentAccVO vo = redisService.get(redisKey, AgentAccVO.class);
        if (vo == null) {
            vo = agentClient.getQrCode(agentId);
            if(vo != null){
                redisService.set(redisKey, vo, 2, TimeUnit.HOURS);
            }
        }
        return vo;
    }

    /**
     *  获取总控的维护配置
     * @return
     */
    private SystemStatusInfoVO getSystemStatusInfo(){
        String controllerKey = RedisKeyPrefix.getControllerKey();
        SystemStatusInfoVO vo = redisService.get(controllerKey, SystemStatusInfoVO.class);
        if (null == vo) {
            String key = "EnjoinLogon";
            vo = accountsClient.getSystemStatusInfo(key);
            if(vo != null){
                redisService.set(controllerKey, vo, 2, TimeUnit.HOURS);
            }
        }
        return vo;
    }

    private Map<String, Object> handleSwitch(Integer agentId, String registerMachine, AgentAccVO agentAccVO) {
        Map<String, Object> data = new HashMap<>();
        //总控的维护
        SystemStatusInfoVO systemStatusInfo = getSystemStatusInfo();
        boolean flag = false;
        if (systemStatusInfo.getStatusValue().compareTo(BigDecimal.ZERO) == 1) {//总后台statusValue为1时开启系统维护
            flag = true;
        }

        //获取业主配置
        List<AgentSystemStatusInfoVO> agentSystemList = getAgentSystemStatusInfo(agentId);
        for (AgentSystemStatusInfoVO vo : agentSystemList) {
            //是否系统维护
            if (!flag && vo.getStatusName().equals(AgentSystemEnum.EnjoinLogon.getName())
                    && vo.getStatusValue().compareTo(BigDecimal.ZERO) == 0) {
                flag = true;
            }
            //绑定手机
            if (vo.getStatusName().equals(AgentSystemEnum.BindMobileSend.getName())) {
                data.put("bindMobileSend", vo.getStatusValue());
            }
            //注册填写真实姓名
            if (vo.getStatusName().equals(AgentSystemEnum.ShowRealName.getName())) {
                data.put("ShowRealName", vo.getIsShow() == 0);
            }
            //注册时银行卡开关
            if (vo.getStatusName().equals(AgentSystemEnum.BANKOPEN.getName())) {
                data.put("BankOpen", vo.getStatusValue().compareTo(BigDecimal.ZERO) == 0);
            }
            //注册时手机号开关
            if (vo.getStatusName().equals(AgentSystemEnum.REGISTEREDPHONEOPEN.getName())) {
                data.put("RegisteredPhoneOpen", vo.getStatusValue().compareTo(BigDecimal.ZERO) == 0);
            }
            //注册帐号开关
            if (vo.getStatusName().equals(AgentSystemEnum.REGISTERACCOUNTOPEN.getName())) {
                data.put("RegisterAccountOpen", vo.getStatusValue().compareTo(BigDecimal.ZERO) == 0);
            }
            //注册界面赠送金币图标开关
            if (vo.getStatusName().equals(AgentSystemEnum.ZCJMZSJBTBOPEN.getName())) {
                data.put("goldGiftCount", vo.getGoldGiftCount());
                data.put("optionButton", vo.getOptionButton());//0 不选 1 账号 2 手机号 3 双选
            }
            //提现时输入余额宝密码开关
            if (vo.getStatusName().equals(AgentSystemEnum.TXYEBPASSWORDOPEN.getName())) {
                data.put("TXYEBpasswordOpen", vo.getStatusValue().compareTo(BigDecimal.ZERO) == 0);
            }
            //余额宝密码开关
            if (vo.getStatusName().equals(AgentSystemEnum.YEBPASSWORDOPEN.getName())) {
                data.put("YEBpasswordOpen", vo.getStatusValue().compareTo(BigDecimal.ZERO) == 0);
            }
            //如果总控没有维护,并且业主维护的时候
            if (vo.getStatusName().equals(AgentSystemEnum.VIPOpen.getName())) {
                data.put("vip", vo.getStatusValue().compareTo(BigDecimal.ZERO) == 0);
            }
            //邮件系统是否开启
            if (vo.getStatusName().equals(AgentSystemEnum.MailOpen.getName())) {
                data.put("mail", vo.getStatusValue().compareTo(BigDecimal.ZERO) == 0);
            }
            //签到
            if (vo.getStatusName().equals(AgentSystemEnum.SignOpen.getName())) {
                data.put("signUp", vo.getStatusValue().compareTo(BigDecimal.ZERO) == 0);
            }
            //修改密码开关
            if (vo.getStatusName().equals(AgentSystemEnum.ResetPwd.getName())) {
                data.put("canResetdhmm", vo.getStatusValue().compareTo(BigDecimal.ZERO) == 0);
            }
            //活动展示
            if (vo.getStatusName().equals(AgentSystemEnum.ActivityOpen.getName())) {
                data.put("ActivityOpen", vo.getStatusValue().compareTo(BigDecimal.ZERO) == 0);
            }
            //提现展示
            if (vo.getStatusName().equals(AgentSystemEnum.ApplyOrderOpen.getName())) {
                data.put("ApplyOrderOpen", vo.getStatusValue().compareTo(BigDecimal.ZERO) == 0);
            }
            //余额宝是否开启
            if (vo.getStatusName().equals(AgentSystemEnum.YebOpen.getName())) {
                data.put("yebiIsopen", vo.getStatusValue().compareTo(BigDecimal.ZERO) == 0);
            }
            if (vo.getStatusName().equals(AgentSystemEnum.WXDLOpen.getName())) {
                data.put("wxdlopen", vo.getStatusValue().compareTo(BigDecimal.ZERO) == 0);
            }
            if (vo.getStatusName().equals(AgentSystemEnum.SJZCOpen.getName())) {
                data.put("sjzcopen", vo.getStatusValue().compareTo(BigDecimal.ZERO) == 0);
            }
            //红包开关
            if (vo.getStatusName().equals(AgentSystemEnum.REDEVENLOPE.getName())) {
                data.put("redEnvelope", vo.getStatusValue().compareTo(BigDecimal.ZERO) == 0);
            }
            //竞价包开启时间
            if (vo.getStatusName().equals(AgentSystemEnum.BIDPACKAGEOPEN.getName())) {
                data.put("BidPackageOpen", vo.getStatusValue());
            }
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
        //系统维护开关
        data.put("Maitance", flag);
        return data;
    }

}
