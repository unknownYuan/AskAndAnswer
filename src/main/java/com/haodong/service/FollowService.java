package com.haodong.service;

//import com.haodong.util.redisssionCluster;
import com.haodong.util.RedisKeyUtil;
import com.haodong.util.RedisssionCluster;
import org.redisson.api.RTransaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Service
public class FollowService {
    @Autowired
    RedisssionCluster redisssionCluster;

    /**
     * 用户关注了某个实体,可以关注问题,关注用户,关注评论等任何实体
     * @param userId
     * @param entityType
     * @param entityId
     * @return
     */
    public boolean follow(int userId, int entityType, int entityId) {
        String followerKey = RedisKeyUtil.getFollowerKey(entityType, entityId);
        String followeeKey = RedisKeyUtil.getFolloweeKey(userId, entityType);
        Date date = new Date();
        // 实体的粉丝增加当前用户
//        Jedis jedis = redisssionCluster.getJedis();
//        Transaction tx = redisssionCluster.multi(jedis);
        RTransaction tx = redisssionCluster.getTransaction();
//        tx.getSet()
        redisssionCluster.zadd(tx, followerKey, date.getTime(), String.valueOf(userId));
        // 当前用户对这类实体关注+1
        redisssionCluster.zadd(tx, followeeKey, date.getTime(), String.valueOf(entityId));
        redisssionCluster.commit(tx);
        return true;
//        return ret.size() == 2 && (Long) ret.get(0) > 0 && (Long) ret.get(1) > 0;
    }

    /**
     * 取消关注
     * @param userId
     * @param entityType
     * @param entityId
     * @return
     */
    public boolean unfollow(int userId, int entityType, int entityId) {
        String followerKey = RedisKeyUtil.getFollowerKey(entityType, entityId);
        String followeeKey = RedisKeyUtil.getFolloweeKey(userId, entityType);
        Date date = new Date();
//        Jedis jedis = redisssionCluster.getJedis();
        RTransaction tx = redisssionCluster.getTransaction();
        // 实体的粉丝增加当前用户
        redisssionCluster.zrem(tx, followerKey, String.valueOf(userId));
        // 当前用户对这类实体关注-1
        redisssionCluster.zrem(tx, followeeKey, String.valueOf(entityId));
//        List<Object> ret = redisssionCluster.exec(tx, jedis);
        redisssionCluster.commit(tx);
//        return ret.size() == 2 && (Long) ret.get(0) > 0 && (Long) ret.get(1) > 0;
        return true;
    }

    public List<Integer> getFollowers(int entityType, int entityId, int count) {
        String followerKey = RedisKeyUtil.getFollowerKey(entityType, entityId);
        return getIdsFromSet(redisssionCluster.zrevrange(followerKey, 0, count));
    }

    public List<Integer> getFollowers(int entityType, int entityId, int offset, int count) {
        String followerKey = RedisKeyUtil.getFollowerKey(entityType, entityId);
        return getIdsFromSet(redisssionCluster.zrevrange(followerKey, offset, offset+count));
    }

    public List<Integer> getFollowees(int userId, int entityType, int count) {
        String followeeKey = RedisKeyUtil.getFolloweeKey(userId, entityType);
        return getIdsFromSet(redisssionCluster.zrevrange(followeeKey, 0, count));
    }

    public List<Integer> getFollowees(int userId, int entityType, int offset, int count) {
        String followeeKey = RedisKeyUtil.getFolloweeKey(userId, entityType);
        return getIdsFromSet(redisssionCluster.zrevrange(followeeKey, offset, offset+count));
    }

    public long getFollowerCount(int entityType, int entityId) {
        String followerKey = RedisKeyUtil.getFollowerKey(entityType, entityId);
        return redisssionCluster.zcard(followerKey);
    }

    public long getFolloweeCount(int userId, int entityType) {
        String followeeKey = RedisKeyUtil.getFolloweeKey(userId, entityType);
        return redisssionCluster.zcard(followeeKey);
    }

    private List<Integer> getIdsFromSet(Set<String> idset) {
        List<Integer> ids = new ArrayList<>();
        for (String str : idset) {
            ids.add(Integer.parseInt(str));
        }
        return ids;
    }

    /**
     *  判断用户是否关注了某个实体
     * @param userId
     * @param entityType
     * @param entityId
     * @return
     */
    public boolean isFollower(int userId, int entityType, int entityId) {
        String followerKey = RedisKeyUtil.getFollowerKey(entityType, entityId);
        return redisssionCluster.zscore(followerKey, String.valueOf(userId)) != null;
    }
}
