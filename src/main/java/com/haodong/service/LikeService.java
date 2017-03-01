package com.haodong.service;

import com.haodong.util.JedisAdapter;
import com.haodong.util.RedisKeyGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by torch on 17-2-28.
 */
@Service
public class LikeService {
    @Autowired
    JedisAdapter jedisAdapter;

    public long getLikeCount(int entityType, int entityId){
        String likeKey = RedisKeyGenerator.getBizLike(entityType, entityId);
        return jedisAdapter.scard(likeKey);
    }
    /**
     * 判断用户是否踩赞
     * @param userId
     * @param entityType
     * @param entityId
     * @return
     */
    public int getLikeStatus(int userId, int entityType, int entityId){
        String likeKey = RedisKeyGenerator.getBizLike(entityType, entityId);
        if(jedisAdapter.sisMember(likeKey, String.valueOf(userId))){
            return 1;
        }else {
            String disLikeKey = RedisKeyGenerator.getBizDislike(entityType, entityId);
            if (jedisAdapter.sisMember(disLikeKey, String.valueOf(userId))) {
                return -1;
            }else {
                return 0;
            }
        }
    }
    /**
     * 点赞service
     *
     * @param userId
     * @param entityType
     * @param entityId
     * @return
     */
    public long like(int userId, int entityType, int entityId) {

        String likeKey = RedisKeyGenerator.getBizLike(entityType, entityId);
        long cnt = jedisAdapter.sadd(likeKey, String.valueOf(userId));

        String disLikeKey = RedisKeyGenerator.getBizDislike(entityType, entityId);
        jedisAdapter.srem(disLikeKey, String.valueOf(userId));

        return jedisAdapter.scard(likeKey);
    }

    /**
     * 踩service
     * @param userId
     * @param entityType
     * @param entityId
     * @return
     */
    public long disLike(int userId, int entityType, int entityId) {
        String disLikeKey = RedisKeyGenerator.getBizDislike(entityType, entityId);
        jedisAdapter.sadd(disLikeKey, String.valueOf(userId));

        String likeKey = RedisKeyGenerator.getBizLike(entityType, entityId);
        jedisAdapter.srem(likeKey, String.valueOf(userId));
        return jedisAdapter.scard(likeKey);
    }
}