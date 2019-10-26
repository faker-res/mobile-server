package com.lzkj.mobile.controller;

import com.alibaba.fastjson.JSONObject;
import com.lzkj.mobile.client.AccountsServiceClient;
import com.lzkj.mobile.client.AgentServiceClient;
import com.lzkj.mobile.config.SystemConstants;
import com.lzkj.mobile.exception.GlobeException;
import com.lzkj.mobile.redis.RedisDao;
import com.lzkj.mobile.redis.RedisKeyPrefix;
import com.lzkj.mobile.util.DESUtil;
import com.lzkj.mobile.util.HttpRequest;
import com.lzkj.mobile.util.MD5Utils;
import com.lzkj.mobile.vo.AccountsInfoVO;
import com.lzkj.mobile.vo.AgentAccVO;
import com.lzkj.mobile.vo.GlobeResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import static com.lzkj.mobile.util.IpAddress.getIpAddress;

@RestController
@RequestMapping("/inter")
@Slf4j
public class InternationalController {

    @Autowired
    private RedisDao redisDao;

    @Autowired
    private AgentServiceClient agentServiceClient;

    @Autowired
    private AccountsServiceClient accountsServiceClient;

    @Value("${access.game}")
    private String accessGame;

    /**
     * API语言切换
     */

    @RequestMapping("/switchLanguage")
    public GlobeResponse<Object> switchLanguage(HttpServletRequest request,Boolean status, Integer agentId, BigDecimal amount, Integer gameId) {

        String ip =getIpAddress(request);
        //true 是切英文版
        String siteCode ="a01";
        AccountsInfoVO accountsInfoVO = accountsServiceClient.getUserInfoByGameId(gameId);
        String account =accountsInfoVO.getH5Account();
        String agentKey = RedisKeyPrefix.getAgentKey(agentId);
        String timestamp = String.valueOf(System.currentTimeMillis());
        String agent = String.valueOf(agentId);
        String orderId =agent+siteCode+timestamp+account;
        AgentAccVO accessAgent = this.redisDao.get(agentKey, AgentAccVO.class);
        if (accessAgent == null) {
            accessAgent = agentServiceClient.getAccessAgent(agentId);
            if (accessAgent == null) {
                throw new GlobeException(SystemConstants.FAIL_CODE, "参数错误");
            }
            redisDao.set(agentKey, accessAgent);
            redisDao.expire(agentKey, 5, TimeUnit.MINUTES);
        }
        log.info("agentInfo{}",accessAgent);
        String md5Signature = MD5Utils.MD5Encode(agent + timestamp + accessAgent.getMd5Key(), "");

        Map<String, Object> data = new LinkedHashMap<>();
        //获取游戏地址
        Random random = new Random();
        String[] gameUrl = this.accessGame.split(",");
        String url= gameUrl[random.nextInt(gameUrl.length)];
        if (status) {
            data.put("op", "50");
        } else {
            data.put("op", "10");
        }
        data.put("orderId", orderId);
        data.put("siteCode", siteCode);
        data.put("money",0);
        data.put("ip",ip);
        data.put("account", account);
        String dParam = DESUtil.encrypt(JSONObject.toJSONString(data), accessAgent.getDesKey());

        String param = "agent=" + agent + "&timestamp=" + timestamp + "&param=" + dParam + "&s=" + md5Signature;

        log.info("send to api center：" + url + "?" + data);
        String msg = HttpRequest.sendPost(url, param);
        log.info("return data {}", msg);
        JSONObject json= (JSONObject) JSONObject.parse(msg);
        GlobeResponse globeResponse =new GlobeResponse();
        globeResponse.setData(json.get("data"));
        return globeResponse;
    }

    public static void main(String[] args) {
        Map<String, Object> data = new LinkedHashMap<>();
        String timestamp = String.valueOf(System.currentTimeMillis());
        String orderId = 106 + "a01" + timestamp + "fsdf34234";
        String url="";
        if (true) {
            url = "https://PT002h242a2.mu622.com/channel";
            data.put("op", "50");
        } else {
            url= "https://PT002h242a2.mu622.com/channel";
            data.put("op", "10");
        }
        data.put("op", "50");
        data.put("orderId", orderId);
        data.put("account", "fsdf34234");
        data.put("siteCode", "a01");
        data.put("money", 3075);
        data.put("gameId",701884);
        String md5Signature = MD5Utils.MD5Encode(106 + timestamp + "95888888","");
        String dParam = DESUtil.encrypt(JSONObject.toJSONString(data), "95888888");

        String param = "agent=" + "106" + "&timestamp=" + timestamp + "&param=" + dParam + "&s=" + md5Signature;

        log.info("send to api center：" + url + "?" + param);
        String msg = HttpRequest.sendPost(url, param);
        log.info("return data {}", msg);
    }
}
