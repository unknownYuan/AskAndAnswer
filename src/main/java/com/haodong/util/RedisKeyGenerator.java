package com.haodong.util;

/**
 * Created by torch on 17-2-28.
 */
public class RedisKeyGenerator {
    private static final String BIZ_TIMELINE = "TIMELINE";
    private static String SPLIT = ":";
    private static String BIZ_LIKE = "LIKE";
    private static String BIZ_DISLIKE = "DISLIKE";
    private static String BIZ_EVENTQUEUE = "EVENT_QUEUE";
    private static String BIZ_FOLLOWER = "FOLLOWER";
    private static String BIZ_FOLLOWEE= "FOLLOWEE";
    private static String comment_question = "comment_question";
    private static String comment_comment = "comment_comment";

    //粉丝的key,通过这个键可以获得我的粉丝集合
    public static String getFollowerKey(int entityType, int entityId){
        return BIZ_FOLLOWER + SPLIT + entityType + SPLIT + entityId;
    }

    //关注对象的key,哪个用户（userId）,关注的实体类型是什么（entityType）
    public static String getFolloweeKey(int userId, int entityType){
        return BIZ_FOLLOWEE + SPLIT + userId + SPLIT + entityType;
    }

    public static String getBizLike(int entityType, int entityId) {
        return BIZ_LIKE + SPLIT + entityType + SPLIT + entityId;
    }

    public static String getBizDislike(int entityType, int entityId) {
        return BIZ_DISLIKE + SPLIT + entityType + SPLIT + entityId;
    }

    public static String getBizEventqueue(){
        return BIZ_EVENTQUEUE;
    }

    public static String getTimeLineKey(int userId) {
        return BIZ_TIMELINE + SPLIT + String.valueOf(userId);
    }

    public static String getCommentQuestionKey(int followerId) {
        return comment_question + SPLIT + followerId;
    }

    public static String getCommentCommentKey(int entityOwnerId) {
        return comment_comment + SPLIT + entityOwnerId;
    }
}
