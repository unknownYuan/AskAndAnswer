package com.haodong.service;

import com.haodong.model.User;
import com.haodong.util.JedisAdapter;
import com.haodong.util.RedisKeyGenerator;
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
    JedisAdapter jedisAdapter;

    @Autowired
    UserService userService;
    /**
     * 关注
     *
     * @param userId
     * @param entityType
     * @param entityId
     * @return
     */
    public boolean follow(int userId, int entityType, int entityId) {
        //我关注了它，它的粉丝列表里面有我，这个key是以它的特征生成
        String followerKey = RedisKeyGenerator.getFollowerKey(entityType, entityId);
        //我关注了它，我关注的问题列表或者粉丝列表里面有它，这个key是以我的特征生成
        String followeeKey = RedisKeyGenerator.getFolloweeKey(userId, entityType);
        Date date = new Date();
        Jedis jedis = jedisAdapter.getJedis();
        Transaction tx = jedisAdapter.multi(jedis);

        //把自己放到关注对象的粉丝列表里面
        tx.zadd(followerKey, date.getTime(), String.valueOf(userId));
        //将关注对象放到自己的关注列表中
        tx.zadd(followeeKey, date.getTime(), String.valueOf(entityId));
        List<Object> ret = jedisAdapter.exec(tx, jedis);
        return ret.size() == 2 && (Long) ret.get(0) > 0 && (Long) ret.get(1) > 0;
    }

    /**
     * 取消关注
     *
     * @param userId
     * @param entityType
     * @param entityId
     * @return
     */
    public boolean unfollow(int userId, int entityType, int entityId) {
        //我关注了哪些人
        String followerKey = RedisKeyGenerator.getFollowerKey(entityType, entityId);
        //哪些粉丝关注了我
        String followeeKey = RedisKeyGenerator.getFolloweeKey(userId, entityType);

        Jedis jedis = jedisAdapter.getJedis();
        Transaction tx = jedisAdapter.multi(jedis);

        tx.zrem(followerKey, String.valueOf(userId));
        tx.zrem(followeeKey, String.valueOf(entityId));
        List<Object> ret = jedisAdapter.exec(tx, jedis);
        return ret.size() == 2 && (Long) ret.get(0) > 0 && (Long) ret.get(1) > 0;
    }

    //将字符串集合转化为整数列表
    private List<Integer> getIdsFromSet(Set<String> idSet) {
        List<Integer> ids = new ArrayList<>();
        for (String str :
                idSet) {
            ids.add(Integer.parseInt(str));
        }
        return ids;
    }

    //查看自己有哪些粉丝
    public List<Integer> getFollowers(int entityType, int entityId, int count) {
        String followerKey = RedisKeyGenerator.getFollowerKey(entityType, entityId);
        return getIdsFromSet(jedisAdapter.zrange(followerKey, 0, count));
    }

    //返回粉丝列表
    public List<Integer> getFollowers(int entityType, int entityId, int offset, int count) {
        String followerKey = RedisKeyGenerator.getFollowerKey(entityType, entityId);
        return getIdsFromSet(jedisAdapter.zrevrange(followerKey, offset, offset + count));
    }
    public List<User> getFollowerUsers(int entityType, int entityId, int offset, int count) {
        String followerKey = RedisKeyGenerator.getFollowerKey(entityType, entityId);
        List<Integer> ids = getIdsFromSet(jedisAdapter.zrevrange(followerKey, offset, offset + count));
        List<User> users = new ArrayList<>();
        for (Integer id:
             ids) {
            User user = userService.getUser(id);
            if(user != null){
                users.add(user);
            }
        }
        return users;
    }

    //返回粉丝数目
    public long getFollowerCount(int entityType, int entityId) {
        String key = RedisKeyGenerator.getFollowerKey(entityType, entityId);
        return jedisAdapter.zcard(key);
    }

    //返回
    public long getFolloweeCount(int userId, int entityId) {
        String key = RedisKeyGenerator.getFolloweeKey(userId, entityId);
        return jedisAdapter.zcard(key);
    }

    //比如：我是某个问题的关注者
    public boolean isFollower(int userId, int entityType, int entityId) {
        String key = RedisKeyGenerator.getFollowerKey(entityType, entityId);
        //问题为key，关注这个问题的人做value
        //错误的写法：用sisMember()函数来判断。注意set和sorted set的区别
        //return jedisAdapter.sisMember(key, String.valueOf(userId)) ;
        return jedisAdapter.zscore(key, String.valueOf(userId)) != null;
    }

    /**
     * 得到我的关注对象列表
     *
     * @param userId
     * @param entityType
     * @param offset
     * @param count
     * @return
     */
    public List<Integer> getFollowees(int userId, int entityType, int offset, int count) {
        String key = RedisKeyGenerator.getFolloweeKey(userId, entityType);
        return getIdsFromSet(jedisAdapter.zrevrange(key, offset, offset + count));
    }
}
