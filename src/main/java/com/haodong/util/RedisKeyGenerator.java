package com.haodong.util;

/**
 * Created by torch on 17-2-28.
 */
public class RedisKeyGenerator {
    private static String SPILT = ":";
    private static String BIZ_LIKE = "LIKE";
    private static String BIZ_DISLIKE = "DISLIKE";
    private static String BIZ_EVENTQUEUE = "EVENT_QUEUE";
    private static String BIZ_FOLLOWER = "FOLLOWER";
    private static String BIZ_FOLLOWEE= "FOLLOWEE";

    //粉丝的key,通过这个键可以获得我的粉丝集合
    public static String getFollowerKey(int entityType, int entityId){
        return BIZ_FOLLOWER + SPILT + entityType + SPILT + entityId;
    }

    //关注对象的key,哪个用户（userId）,关注的实体类型是什么（entityType）
    public static String getFolloweeKey(int userId, int entityType){
        return BIZ_FOLLOWEE + SPILT + userId + SPILT + entityType;
    }

    public static String getBizLike(int entityType, int entityId) {
        return BIZ_LIKE + SPILT + entityType + SPILT + entityId;
    }

    public static String getBizDislike(int entityType, int entityId) {
        return BIZ_DISLIKE + SPILT + entityType + SPILT + entityId;
    }

    public static String getBizEventqueue(){
        return BIZ_EVENTQUEUE;
    }
}
