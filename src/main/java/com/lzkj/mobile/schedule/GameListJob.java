package com.lzkj.mobile.schedule;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.lzkj.mobile.client.AgentServiceClient;
import com.lzkj.mobile.client.PlatformServiceClient;
import com.lzkj.mobile.redis.RedisDao;
import com.lzkj.mobile.redis.RedisKeyPrefix;
import com.lzkj.mobile.vo.AgentMobileKindConfigVO;
import com.lzkj.mobile.vo.PlatformVO;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class GameListJob {
	
	@Autowired
	private AgentServiceClient agentServiceClient;
	
	@Autowired
	private PlatformServiceClient platformServiceClient;
	
	@Autowired
	private RedisDao redisService;
	
//    public List<PlatformVO>  getAgentGameListByGameTypeItem(@RequestParam("agentId")Integer agentId);
//    public List<AgentMobileKindConfigVO> getAgentGameByGameTypeItem(@RequestParam("agentId")Integer agentId);
	/**
	 * 将游戏列表写入到ftp服务器
	 */
	@Scheduled(cron = "0 3 0 * * ?")
	public void uploadGameList() {
		List<Integer> agentList = agentServiceClient.getALLAgent();
		if(agentList.size() <= 0) {
			log.info("将游戏列表写入到ftp服务器没有取到业主信息"+new Date());
			return;
		}
		Map<String,List> data = new HashMap<String,List>();
		String rediskey = "";
		for (Integer agentId : agentList) {
			List<PlatformVO> platformVo;
			List<AgentMobileKindConfigVO> thirdList;
			rediskey = RedisKeyPrefix.getAgentGameListByGameTypeItemKeyStatus(agentId);
			Integer status = redisService.get(rediskey, Integer.class);
			if(null == status) {
				redisService.set(rediskey, 0);
				redisService.expire(rediskey, 2, TimeUnit.HOURS);
				platformVo = platformServiceClient.getAgentGameListByGameTypeItem(agentId);
				rediskey = RedisKeyPrefix.getAgentGameListByGameTypeItemKey(agentId);
				data.put("platfromList",platformVo);
				redisService.set(rediskey, platformVo);
				redisService.expire(rediskey, 2, TimeUnit.HOURS);
			}
			if(1 == status) {
				redisService.set(rediskey, 0);
				redisService.expire(rediskey, 2, TimeUnit.HOURS);
				rediskey = RedisKeyPrefix.getAgentGameListByGameTypeItemKey(agentId);
				platformVo = redisService.getList(rediskey, PlatformVO.class);
				data.put("platfromList",platformVo);
			}
			rediskey = RedisKeyPrefix.getAgentGameListByGameTypeItemKeyStatus(agentId);
			if(null == status) {
				redisService.set(rediskey, 0);
				redisService.expire(rediskey, 2, TimeUnit.HOURS);
				platformVo = platformServiceClient.getAgentGameListByGameTypeItem(agentId);
				rediskey = RedisKeyPrefix.getAgentGameListByGameTypeItemKey(agentId);
//				data.put("ThirdGameList",thirdList);
				redisService.set(rediskey, platformVo);
				redisService.expire(rediskey, 2, TimeUnit.HOURS);
			}
			if(1 == status) {
				redisService.set(rediskey, 0);
				redisService.expire(rediskey, 2, TimeUnit.HOURS);
				rediskey = RedisKeyPrefix.getAgentGameListByGameTypeItemKey(agentId);
				platformVo = redisService.getList(rediskey, PlatformVO.class);
//			    data.put("ThirdGameList",thirdList);
			}
		}
	}
	
	@Async
	public void uploadFTPGameList() {
		
	}
}
