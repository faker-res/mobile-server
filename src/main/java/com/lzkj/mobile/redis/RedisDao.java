package com.lzkj.mobile.redis;

import com.alibaba.fastjson.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;


@Component
public class RedisDao {
	
	@Autowired
    private RedisTemplate<String,String> redisTemplate;
	
	public void putMap(String key, Map<String, Object> map) {
		redisTemplate.opsForHash().putAll(key,map);
	}
	
	public Object getMap(String mapKey, String valueKey) {
		return redisTemplate.opsForHash().get(mapKey, valueKey);
	}
	
	/**
	 * 获取数据
	 * @Title: get  
	 * @param key
	 * @param clazz
	 * @author Henry  
	 * @date 2018年8月19日
	 */
	public <T> T get(String key,Class<T> clazz){
		   String string = redisTemplate.opsForValue().get(key);
		   if(string == null) return null;
		   T o = JsonUtil.parseObject(string, clazz);
		   
		return o;
	}
	
	/**
	 * 获取集合对象
	 */
	public <T> List<T> getList(String key ,Class<T> clazz){
		 
		String string = redisTemplate.opsForValue().get(key);
		if(string == null) return null;
		List<T> parseArray = JSONArray.parseArray(string, clazz);
		return parseArray;
	} 
	
	/**
	 * 存数据
	 */
	public void set(String key,Object value) {
		redisTemplate.opsForValue().set(key, JsonUtil.parseJsonString(value));
	}
	
	/**
	 * 存数据,同时设置时间
	 */
	public void setByExpireTime(String key,Object value) {
		redisTemplate.opsForValue().set(key, JsonUtil.parseJsonString(value));
		expire(key,1,TimeUnit.HOURS);
	}
	
	/**
	 * 删除key
	 */
	public void delete(String key) {
		redisTemplate.delete(key);
	}
	
	/**
	 *获取所有的key
	 */
	public Set<String> keys(String pattern) {
		return redisTemplate.keys(pattern);
	}
	

	/**
	 * 设置key的超时时间
	 */
	public void expire(String key,long timeOut,TimeUnit unit) {
		redisTemplate.expire(key, timeOut, unit);
	}
	
	public RedisConnection getConnection() {
		return redisTemplate.getConnectionFactory().getConnection();
	}
	
}
