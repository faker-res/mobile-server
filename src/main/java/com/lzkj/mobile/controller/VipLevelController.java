package com.lzkj.mobile.controller;

import com.lzkj.mobile.client.AccountsServiceClient;
import com.lzkj.mobile.client.PlatformServiceClient;
import com.lzkj.mobile.client.TreasureServiceClient;
import com.lzkj.mobile.config.SystemConstants;
import com.lzkj.mobile.exception.GlobeException;
import com.lzkj.mobile.util.HttpRequest;
import com.lzkj.mobile.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/vipLevel")
@Slf4j
public class VipLevelController {

    @Autowired
    private PlatformServiceClient platformServiceClient;

    @Autowired
    private AccountsServiceClient accountsServiceClient;

    @Autowired
    private TreasureServiceClient treasureServiceClient;

    @Value("${server.url}")
    private String serverUrl;

    /**
     * 获取玩家的VIP排行榜
     *
     * @param agentId
     * @param gameId
     * @return
     */
    @RequestMapping("/getVipRank")
    public GlobeResponse getVipRank(Integer agentId, Integer gameId) {
        if (null == agentId || agentId == 0) {
            throw new GlobeException(SystemConstants.FAIL_CODE, "业主标识错误");
        }
        if (null == gameId || gameId == 0) {
            throw new GlobeException(SystemConstants.FAIL_CODE, "玩家游戏标识错误");
        }
        List<AccountsLevelVO> list = accountsServiceClient.getVipRank(agentId);
        AccountsLevelVO accountsLevelVO = accountsServiceClient.getPlayerVipInfo(gameId);
        BigDecimal score = accountsLevelVO.getVipIntegral().setScale(0, BigDecimal.ROUND_DOWN);
        accountsLevelVO.setVipIntegral(score);
        Boolean flag = false;
        if (accountsLevelVO.getVipLevel() != 0) {
            for (AccountsLevelVO a : list) {
                if (a.getGameId().equals(gameId)) {
                    flag = true;
                    break;
                }
            }
        }
        GlobeResponse<Object> globeResponse = new GlobeResponse<>();
        Map<String, Object> data = new HashMap<>();
        data.put("list", list);
        data.put("flag", flag);
        if (flag) {
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).getGameId().equals(gameId)) {
                    data.put("sortId", i + 1);
                }
            }
        }
        data.put("player", accountsLevelVO);
        globeResponse.setData(data);
        return globeResponse;
    }

    /**
     * VIP 等级福利
     */
    @RequestMapping("/getVipRankWelfare")
    public GlobeResponse getVipRankWelfare(Integer agentId, Integer gameId) {
        if (null == agentId || agentId == 0) {
            throw new GlobeException(SystemConstants.FAIL_CODE, "业主标识错误");
        }
        if (null == gameId || gameId == 0) {
            throw new GlobeException(SystemConstants.FAIL_CODE, "玩家游戏标识错误");
        }
        VipWelfareVO vipWelfareVO = platformServiceClient.getVipRankWelfare(agentId, gameId);
        GlobeResponse<Object> globeResponse = new GlobeResponse<>();
        globeResponse.setData(vipWelfareVO);
        return globeResponse;
    }

    /**
     * 获取游戏用户领取VIP奖励信息
     * 日周月年的配置信息
     */
    @RequestMapping("/getVipConfig")
    public GlobeResponse getVipConfig(Integer agentId, Integer userId) {
        if (null == agentId || agentId == 0) {
            throw new GlobeException(SystemConstants.FAIL_CODE, "业主标识错误");
        }
        if (null == userId || userId == 0) {
            throw new GlobeException(SystemConstants.FAIL_CODE, "玩家游戏标识错误");
        }
        Map<String, Object> param = platformServiceClient.getVipConfig(agentId, userId);
        param.put("WashCode", "0.6");
        GlobeResponse<Object> globeResponse = new GlobeResponse<>();
        globeResponse.setData(param);
        return globeResponse;
    }

    /**
     * 获取业主的VIP积分关系表
     */
    @RequestMapping("/getVipRelation")
    public GlobeResponse getVipRelation(Integer agentId) {
        if (null == agentId || agentId == 0) {
            throw new GlobeException(SystemConstants.FAIL_CODE, "业主标识错误");
        }
        List<VipIntegrealConfigVO> list = platformServiceClient.getVipRelation(agentId);
        GlobeResponse<Object> globeResponse = new GlobeResponse<>();
        globeResponse.setData(list);
        return globeResponse;
    }

    /**
     * 获取游戏用户领取VIP奖励信息
     */
    @RequestMapping("/getUserReceiveInfo")
    public GlobeResponse<Object> getUserReceiveInfo(Integer userId) {
        if (userId == null || userId == 0) {
            throw new GlobeException(SystemConstants.FAIL_CODE, "用户信息错误!");
        }
        List<VIPReceiveInfoVO> list = platformServiceClient.getUserReceiveInfo(userId);
        GlobeResponse<Object> globeResponse = new GlobeResponse<>();
        globeResponse.setData(list);
        return globeResponse;
    }

    /**
     * 领取等级奖励
     */
    @RequestMapping("/levelReward")
    public GlobeResponse<Object> levelReward(Integer userId, Integer level, BigDecimal reward) {
        if (userId == null || userId == 0) {
            throw new GlobeException(SystemConstants.FAIL_CODE, "代用户信息错误!");
        }
        if (level == null || level == 0) {
            throw new GlobeException(SystemConstants.FAIL_CODE, "领取等级错误");
        }
        String flag = platformServiceClient.levelReward(userId, level, reward);
        GlobeResponse<Object> globeResponse = new GlobeResponse<>();
        if (flag.equals("领取成功!")) {
            globeResponse.setData(true);
            Map<String, BigDecimal> map = treasureServiceClient.getUserGameScore(userId);
            Object score = map.get("score");
            Object insureScore = map.get("insureScore");
            String msg = "{\"msgid\":7,\"userId\":" + userId + ", \"score\":" + score + ",\"insuranceScore\":" + insureScore +
                    ", \"VipLevel\":" + level + ", \"type\":" + 0 + ", \"Charge\":" + 0 + "}";
            log.info("调用金额变更指令:{}, 返回：" + HttpRequest.sendPost(this.serverUrl, msg), msg);
        } else {
            globeResponse.setCode("-1");
            globeResponse.setMsg(flag);
        }
        return globeResponse;
    }

    /**
     * 领取积分奖励
     */
    @RequestMapping("/pointReward")
    public GlobeResponse<Object> pointReward(Integer userId, Integer type) {
        if (userId == null || userId == 0) {
            throw new GlobeException(SystemConstants.FAIL_CODE, "代用户信息错误!");
        }
        if (type == null || type == 0) {
            throw new GlobeException(SystemConstants.FAIL_CODE, "领取类型错误");
        }
        String flag = platformServiceClient.pointReward(userId, type);
        GlobeResponse<Object> globeResponse = new GlobeResponse<>();
        if (flag.equals("领取成功！")) {
            globeResponse.setData(true);
            Map<String, BigDecimal> map = treasureServiceClient.getUserGameScore(userId);
            Object score = map.get("score");
            Object insureScore = map.get("insureScore");
            AccountsLevelVO accountsLevelVO = accountsServiceClient.getPlayerLevel(userId);
            Integer level = accountsLevelVO.getVipLevel();
            String msg = "{\"msgid\":7,\"userId\":" + userId + ", \"score\":" + score + ",\"insuranceScore\":" + insureScore +
                    ", \"VipLevel\":" + level + ", \"type\":" + 0 + ", \"Charge\":" + 0 + "}";
            log.info("调用金额变更指令:{}, 返回：" + HttpRequest.sendPost(this.serverUrl, msg), msg);
            globeResponse.setData(true);
        } else {
            globeResponse.setCode("-1");
            globeResponse.setMsg(flag);
        }
        return globeResponse;
    }
}
