package com.haodong.util;

import org.redisson.Redisson;
import org.redisson.api.RTransaction;
import org.redisson.api.TransactionOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Service
public class RedisssionCluster {

    private static final Logger logger = LoggerFactory.getLogger(RedisssionCluster.class);

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private Redisson redisson;

    public static void print(int index, Object obj) {
        System.out.println(String.format("%d, %s", index, obj.toString()));
    }

    public long sadd(String key, String value) {
        try {

            return stringRedisTemplate.opsForSet().add(key, value);
        } catch (Exception e) {
            logger.error("发生异常" + e.getMessage());
            e.printStackTrace();
        }
        return 0L;
    }

    public Long srem(String key, String value) {
        try {
            return stringRedisTemplate.opsForSet().remove(key, value);
        } catch (Exception e) {
            logger.error("发生异常" + e.getMessage());
            e.printStackTrace();
        }
        return 0L;
    }

    public long scard(String key) {
        try {
            return stringRedisTemplate.opsForSet().size(key);
        } catch (Exception e) {
            logger.error("发生异常" + e.getMessage());
        }
        return 0;
    }

    public boolean sismember(String key, String value) {

        try {

            return stringRedisTemplate.opsForSet().isMember(key, value);
        } catch (Exception e) {
            logger.error("发生异常" + e.getMessage());
        }
        return false;
    }

    public List<String> brpop(int timeout, String key) {
        try {
            stringRedisTemplate.opsForList().rightPop(key, timeout, TimeUnit.SECONDS);
        } catch (Exception e) {
            logger.error("发生异常" + e.getMessage());
        }
        return new ArrayList<String>();
    }

    public long lpush(String key, String value) {
        try {
            return stringRedisTemplate.opsForList().leftPush(key, value);
        } catch (Exception e) {
            logger.error("发生异常" + e.getMessage());
        }
        return 0;
    }

    public List<String> lrange(String key, int start, int end) {
        try {
            return stringRedisTemplate.opsForList().range(key, start, end);
        } catch (Exception e) {
            logger.error("发生异常" + e.getMessage());
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public boolean zadd(RTransaction transaction, String key, double score, String value) {
        try {
            return stringRedisTemplate.opsForZSet().add(key, value, score);
        } catch (Exception e) {
            logger.error("发生异常" + e.getMessage());
        }
        return false;
    }

    public long zrem(RTransaction tx, String key, String value) {
        try {
            return stringRedisTemplate.opsForZSet().remove(key, value);
        } catch (Exception e) {
            logger.error("发生异常" + e.getMessage());
        }
        return 0;
    }

    public RTransaction getTransaction() {
        try {
            RTransaction transaction =redisson.createTransaction(TransactionOptions.defaults());
            return transaction;
        } catch (Exception e) {
            logger.error("发生异常" + e.getMessage());
        }
        return null;
    }

    public void commit(RTransaction transaction){
        transaction.commit();
    }


    public Set<String> zrange(String key, int start, int end) {
        try {
            return stringRedisTemplate.opsForZSet().range(key, start, end);
        } catch (Exception e) {
            logger.error("发生异常" + e.getMessage());
        }
        return new HashSet<>();
    }

    public Set<String> zrevrange(String key, int start, int end) {
        try {
            return stringRedisTemplate.opsForZSet().reverseRange(key, start, end);
        } catch (Exception e) {
            logger.error("发生异常" + e.getMessage());
        }
        return null;
    }

    public long zcard(String key) {
        try {
            return stringRedisTemplate.opsForZSet().size(key);
        } catch (Exception e) {
            logger.error("发生异常" + e.getMessage());
        }
        return 0;
    }

    public Double zscore(String key, String member) {
        try {
            return stringRedisTemplate.opsForZSet().score(key, member);
        } catch (Exception e) {
            logger.error("发生异常" + e.getMessage());
        }
        return null;
    }
}
