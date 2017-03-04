package com.haodong.util;

import org.apache.velocity.util.ArrayListWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Transaction;

import java.io.IOException;
import java.util.*;

@Service
public class JedisAdapter implements InitializingBean {
    private static final Logger logger = LoggerFactory.getLogger(JedisAdapter.class);
    //jedis连接池
    private JedisPool jedisPool = null;

    //获得jedis
    public Jedis getJedis() {
        return jedisPool.getResource();
    }

    public long zadd(String key, double score, String value) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            return jedis.zadd(key, score, value);
        } catch (Exception e) {

        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return 0;
    }

    public Transaction multi(Jedis jedis) {
        try {
            return jedis.multi();
        } catch (Exception e) {

        } finally {

        }
        return null;
    }

    public List<Object> exec(Transaction tx, Jedis jedis) {
        try {
            return tx.exec();
        } catch (Exception e) {

        } finally {
            if (tx != null) {
                try {
                    tx.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (jedis != null) {
                jedis.close();
            }
        }
        return new ArrayList<>();//默认返回值是0
    }

    public long sadd(String likeKey, String userId) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();//获取资源
            return jedis.sadd(likeKey, userId);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (jedis != null) {
                jedis.close();//释放资源
            }
        }
        return 0;
    }

    public Set<String> zrange(String key, int start, int end) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();

            return jedis.zrange(key, start, end);
        } catch (Exception e) {

        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return null;
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
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return 0;
    }


    public boolean sisMember(String key, String userId) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            return jedis.sismember(key, userId);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return false;
    }

    public void srem(String disLike, String userId) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.srem(disLike, userId);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    public void lpush(String key, String json) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.lpush(key, json);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    public List<String> brpop(int timeOut, String key) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            return jedis.brpop(timeOut, key);//b--blocking r--remove pop--返回最后一个元素的值
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return null;
    }

    public long zcard(String key) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            return jedis.zcard(key);
        } catch (Exception e) {

        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return 0;
    }

    /**
     * 成员的分数值，以字符串的形式表示
     * @param key
     * @param member
     * @return
     */
    public Double zscore(String key, String member) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.zscore(key, member);
        } catch (Exception e) {

        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return null;
    }

    public Set<String> zrevrange(String key, int start, int end) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.zrevrange(key, start, end);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return new HashSet<String>();
    }
}
