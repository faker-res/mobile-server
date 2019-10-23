package com.lzkj.mobile.controller;

import com.alibaba.fastjson.JSONObject;
import com.lzkj.mobile.client.AgentServiceClient;
import com.lzkj.mobile.config.SystemConstants;
import com.lzkj.mobile.exception.GlobeException;
import com.lzkj.mobile.redis.RedisDao;
import com.lzkj.mobile.redis.RedisKeyPrefix;
import com.lzkj.mobile.util.DESUtil;
import com.lzkj.mobile.util.HttpRequest;
import com.lzkj.mobile.util.MD5Utils;
import com.lzkj.mobile.vo.AgentAccVO;
import com.lzkj.mobile.vo.GlobeResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/inter")
@Slf4j
public class InternationalController {

    @Autowired
    private RedisDao redisDao;

    @Autowired
    private AgentServiceClient agentServiceClient;

    /**
     * API语言切换
     */

    @RequestMapping("/switchLanguage")
    public GlobeResponse<Object> switchLanguage(Boolean status, Integer agentId, String account) {
        //true 是切英文版
        String agentKey = RedisKeyPrefix.getAgentKey(agentId);
        String timestamp = String.valueOf(System.currentTimeMillis());
        String agent = String.valueOf(agentId);
        AgentAccVO accessAgent = this.redisDao.get(agentKey, AgentAccVO.class);
        if (accessAgent == null) {
            accessAgent = agentServiceClient.getAccessAgent(agentId);
            if (accessAgent == null) {
                throw new GlobeException(SystemConstants.FAIL_CODE, "参数错误");
            }
            redisDao.set(agentKey, accessAgent);
            redisDao.expire(agentKey, 5, TimeUnit.MINUTES);
        }

        String md5Signature = MD5Utils.MD5Encode(agent + timestamp + accessAgent.getMd5Key(), "");

        Map<String, String> data = new LinkedHashMap<>();
        String url;
        if (status) {
             url = "https://wt002bnqqi3.mu622.com/channel";
            data.put("op", "50");
        } else {
            url= "https://wt002bnqqi3.mu622.com/channel";
            data.put("op", "10");
        }
        data.put("op", "50");
        data.put("account", account);
        data.put("siteCode", agent);

        String dParam = DESUtil.encrypt(data.toString(), accessAgent.getDesKey());

        String param = "agent=" + agent + "&timestamp=" + timestamp + "&param=" + dParam + "&s=" + md5Signature;

        log.info("发送到api中转中心：" + url + "?" + data);
        String msg = HttpRequest.sendPost(url, param);
        log.info("返回数据{}", msg);
        JSONObject json= (JSONObject) JSONObject.parse(msg);
        GlobeResponse globeResponse =new GlobeResponse();
        globeResponse.setData(json.get("url"));
        return globeResponse;
    }

}
