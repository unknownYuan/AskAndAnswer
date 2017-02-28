package com.haodong.util;

/**
 * Created by torch on 17-2-28.
 */
public class RedisKeyGenerator {
    private static String SPILT = ":";
    private static String BIZ_LIKE = "LIKE";
    private static String BIZ_DISLIKE = "DISLIKE";

    public static String getBizLike(int entityType, int entityId) {
        return BIZ_LIKE + SPILT + entityType + SPILT + entityId;
    }

    public static String getBizDislike(int entityType, int entityId) {
        return BIZ_DISLIKE + SPILT + entityType + SPILT + entityId;
    }
}
