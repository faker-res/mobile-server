package com.lzkj.mobile.schedule;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.SocketException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONArray;
import com.lzkj.mobile.client.AgentServiceClient;
import com.lzkj.mobile.client.PlatformServiceClient;
import com.lzkj.mobile.redis.RedisDao;
import com.lzkj.mobile.redis.RedisKeyPrefix;
import com.lzkj.mobile.vo.AgentMobileKindConfigVO;
import com.lzkj.mobile.vo.PlatformVO;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class GameListJob implements ApplicationRunner {
	
	@Autowired
	private AgentServiceClient agentServiceClient;
	
	@Autowired
	private PlatformServiceClient platformServiceClient;
	
	@Autowired
	private RedisDao redisService;
	
	@Value("${gameList.url}")
    private String url;
	
	@Value("${gameList.name}")
    private String name;
	
	@Value("${gameList.password}")
    private String password;
	
	@Value("${gameList.port}")
    private Integer port;
	/**
	 * 将游戏列表写入到ftp服务器
	 */
	@Scheduled(cron = "0 0/3 * * * *")
	public void uploadGameList() {
		log.info("开始写游戏列表到文件服务器");
		FTPClient fc = new FTPClient();
		try {
			fc.connect(url, port); // 连接ftp服务器
			fc.login(name, password); // 登录ftp服务器
			int replyCode = fc.getReplyCode(); // 是否成功登录服务器
			if (!FTPReply.isPositiveCompletion(replyCode)) {
				log.info("============FTP连接失败，停止开始写文件============");
				return;
			}
			List<Integer> agentList = agentServiceClient.getALLAgent();
			if(agentList.size() <= 0) {
				log.info("将游戏列表写入到ftp服务器没有取到业主信息"+new Date());
				return;
			}
			String rediskey = "";
			for (Integer agentId : agentList) {
				Map<String,List> data = new HashMap<String,List>();
				List<PlatformVO> platformVo;
				List<AgentMobileKindConfigVO> thirdList;
				rediskey = RedisKeyPrefix.getGameListStatus(agentId);
				String status = redisService.get(rediskey,  String.class);
				log.info(agentId+ "status:" +status);
				if(StringUtils.isBlank(status)) {
					redisService.set(rediskey, "0");
					redisService.expire(rediskey, 2, TimeUnit.HOURS);
					platformVo = platformServiceClient.getAgentGameListByGameTypeItem(agentId);
					rediskey = RedisKeyPrefix.getAgentGameListByGameTypeItemKey(agentId);
					data.put("platfromList",platformVo);
					redisService.set(rediskey, platformVo);
					redisService.expire(rediskey, 2, TimeUnit.HOURS);
					thirdList = platformServiceClient.getAgentGameByGameTypeItem(agentId);
					rediskey = RedisKeyPrefix.getAgentGameByGameTypeItemKey(agentId);
					data.put("ThirdGameList",thirdList);
					redisService.set(rediskey, thirdList);
					redisService.expire(rediskey, 2, TimeUnit.HOURS);
				}else if("1".equals(status)) {
					redisService.set(rediskey, "0");
					redisService.expire(rediskey, 2, TimeUnit.HOURS);
					rediskey = RedisKeyPrefix.getAgentGameListByGameTypeItemKey(agentId);
					platformVo = redisService.getList(rediskey, PlatformVO.class);
					rediskey = RedisKeyPrefix.getAgentGameByGameTypeItemKey(agentId);
					thirdList = redisService.getList(rediskey, AgentMobileKindConfigVO.class);
				    data.put("ThirdGameList",thirdList);
					data.put("platfromList",platformVo);
				}
				log.info("data.size():"+data.size());
				if(data.size() > 0) {
					uploadFTPGameList(data,agentId,fc);
				}
			}
			
		}catch (Exception e) {
			// TODO: handle exception
			log.info("写文件到游戏列表报错",e);
		} finally {
			if(fc.isConnected()) {
				try {
					fc.logout();
					fc.disconnect();
				} catch (IOException e) {
					e.printStackTrace();
				}			
			}
		}
		log.info("结束写游戏列表到文件服务器");
	}
	
	public void uploadFTPGameList(Map<String,List> map,Integer agentId,FTPClient ftpClient)  {
		try {
			log.info(agentId+"开始上传到文件服务器");
			JSONArray jArray = new JSONArray();
	        jArray.add(map);
	        String data = jArray.toString();
	        log.info(agentId+"data"+data);
			InputStream input = new ByteArrayInputStream(data.getBytes("utf-8"));
			ftpClient.enterLocalPassiveMode();
			ftpClient.setFileType(ftpClient.BINARY_FILE_TYPE);
			if(!ftpClient.storeFile(agentId+".json", input)) {
				log.info("上传文件失败"+agentId);
			}
			input.close();	
		}catch (Exception e) {
			log.info(agentId+"上传到文件服务器报错",e);
		}
		log.info(agentId+"结束上传到文件服务器");
	}

	@Override
	public void run(ApplicationArguments args) throws Exception {
		uploadGameList();		
	}
}
