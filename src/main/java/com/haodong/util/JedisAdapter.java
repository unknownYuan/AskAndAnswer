package com.haodong.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

@Service
public class JedisAdapter implements InitializingBean {
    private static final Logger logger = LoggerFactory.getLogger(JedisAdapter.class);
    //jedis连接池
    private JedisPool jedisPool = null;

    public long sadd(String likeKey, String userId) {
        Jedis jedis = null;
        try {
            jedisPool.getResource();//获取资源
            return jedis.sadd(likeKey, userId);
        } catch (Exception e) {
            logger.error("发生异常" + e.getMessage());
        } finally {
            if (jedis != null) {
                jedis.close();//释放资源
            }
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        jedisPool = new JedisPool("redis://localhost:6379/10");
    }

    /**
     * 获得点赞或者踩的数量
     *
     * @param key
     * @return
     */
    public long scard(String key) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            return jedis.scard(key);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }


    public boolean sisMember(String key, String userId) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            return jedis.sismember(key, userId);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    public void srem(String disLike, String userId) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.srem(disLike, userId);
        } catch (Exception e) {
            logger.error("发生异常" + e.getMessage());
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }
}
