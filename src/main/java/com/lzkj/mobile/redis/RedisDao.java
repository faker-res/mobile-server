package com.lzkj.mobile.redis;

import com.alibaba.fastjson.JSON;
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
    private RedisTemplate<String, String> redisTemplate;

    public void putMap(String key, Map<String, Object> map) {
        redisTemplate.opsForHash().putAll(key, map);
    }

    public Object getMap(String mapKey, String valueKey) {
        return redisTemplate.opsForHash().get(mapKey, valueKey);
    }

    /**
     * 获取数据
     *
     * @param key
     * @Title: get
     * @author
     * @date 2018年8月19日
     */
    public String get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    /**
     * 获取数据
     *
     * @param key
     * @param clazz
     * @Title: get
     * @author Henry
     * @date 2018年8月19日
     */
    public <T> T get(String key, Class<T> clazz) {
        String string = redisTemplate.opsForValue().get(key);
        if (string == null) return null;
        return JSON.parseObject(string, clazz);
    }

    /**
     * 获取集合对象
     */
    public <T> List<T> getList(String key, Class<T> clazz) {
        String string = redisTemplate.opsForValue().get(key);
        if (string == null) return null;
        return JSONArray.parseArray(string, clazz);
    }

    /**
     * 存数据
     */
    public void set(String key, Object value) {
        redisTemplate.opsForValue().set(key, JSON.toJSONString(value));
    }

    /**
     * 存数据,同时设置时间
     */
    public void set(String key, Object value, long timeOut, TimeUnit timeUnit) {
        redisTemplate.opsForValue().set(key, JSON.toJSONString(value));
        expire(key, timeOut, timeUnit);
    }

    /**
     * 上锁
     *
     * @return
     */
    public boolean setIfAbsent(String key, String value, long timeOut, TimeUnit timeUnit) {
        Boolean ifAbsent = redisTemplate.opsForValue().setIfAbsent(key, value);
        expire(key, timeOut, timeUnit);
        return ifAbsent;
    }

    /**
     * 删除key
     */
    public void delete(String key) {
        redisTemplate.delete(key);
    }

    /**
     * 获取所有的key
     */
    public Set<String> keys(String pattern) {
        return redisTemplate.keys(pattern + "*");
    }


    /**
     * 设置key的超时时间
     */
    public void expire(String key, long timeOut, TimeUnit unit) {
        redisTemplate.expire(key, timeOut, unit);
    }

    public RedisConnection getConnection() {
        return redisTemplate.getConnectionFactory().getConnection();
    }

}
