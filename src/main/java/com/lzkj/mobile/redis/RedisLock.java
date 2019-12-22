package com.lzkj.mobile.redis;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

@Slf4j
public class RedisLock implements Lock {

    private RedisTemplate<String,String> redisTemplate;

    // 存储到redis中的锁value
    private static final String LOCK = "LOCK";

    //1000
    public static final int ONE_THOUSAND = 1000;

    // 锁标志对应的key;
    private String key;

    // 锁的有效时间(ms)
    public int expireTime;

    // 上锁状态state
    public volatile boolean isLocked = false;

    public RedisLock(String key, RedisTemplate<String, String> redisTemplate, int expireTime) {
        super();
        this.redisTemplate = redisTemplate;
        this.key = key;
        this.expireTime = expireTime;
    }

    @Override
    public void lock() {
        RedisConnection connection = null;
        try {
            long nowTime = System.currentTimeMillis();
            //不断循环向Master节点请求锁，当请求时间(System.nanoTime() - nano)超过设定的超时时间则放弃请求锁
            //这个可以防止一个客户端在某个宕掉的master节点上阻塞过长时间
            //如果一个master节点不可用了，应该尽快尝试下一个master节点
            connection = redisTemplate.getConnectionFactory().getConnection();
            while ((System.currentTimeMillis() - nowTime) < expireTime * ONE_THOUSAND) {
                if(null == connection){
                    connection = redisTemplate.getConnectionFactory().getConnection();
                }
                //将锁作为key存储到redis缓存中，存储成功则获得锁
                boolean flag = connection.setNX(key.getBytes(), LOCK.getBytes());
                if(flag){
                    //设置锁的有效期，也是锁的自动释放时间，也是一个客户端在其他客户端能抢占锁之前可以执行任务的时间
                    //可以防止因异常情况无法释放锁而造成死锁情况的发生
                    redisTemplate.expire(key, expireTime, TimeUnit.SECONDS);
                    isLocked = true;
                    //上锁成功结束请求
                    break;
                }
                //获取锁失败时，应该在随机延时后进行重试，避免不同客户端同时重试导致谁都无法拿到锁的情况出现
                //睡眠10毫秒后继续请求锁
                Thread.sleep(10);
            }
        } catch (Exception e) {
            log.info("lock 加锁失败----");
            e.printStackTrace();
        }finally{
            if(null != connection){
                connection.close();
                connection = null;
            }
        }
    }

    @Override
    public void unlock() {
        //释放锁
        //不管请求锁是否成功，只要已经上锁，客户端都会进行释放锁的操作
        if (isLocked) {
            redisTemplate.delete(key);
        }
    }

    @Override
    public void lockInterruptibly() throws InterruptedException {
    }

    @Override
    public Condition newCondition() {
        return null;
    }

    @Override
    public boolean tryLock() {
        //系统当前时间，毫秒
        RedisConnection connection = null;
        try {
            //将锁作为key存储到redis缓存中，存储成功则获得锁
            connection = redisTemplate.getConnectionFactory().getConnection();
            boolean flag = connection.setNX(key.getBytes(), LOCK.getBytes());
            if (flag){
                redisTemplate.expire(key, expireTime, TimeUnit.SECONDS);
                isLocked = true;
                return true;
            }
        } catch (Exception e) {
            log.info("tryLock 加锁失败----");
            e.printStackTrace();
        }finally{
            if(null != connection){
                connection.close();
                connection = null;
            }
        }
        return false;
    }

    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        return false;
    }

}
