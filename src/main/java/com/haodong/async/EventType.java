package com.haodong.async;

public enum EventType {
    LIKE_COMMENT(0),
    LOGIN(1),
    MAIL(2),
    FOLLOW_QUESTION(3),
    UNFOLLOW_QUESTION(4),
    DISLIKE_COMMENT(5),
    LIKE_ANSWER(6),
    DISLIKE_ANSWER(7),
    COMMENT_QUESTION(8),
    FOLLOW_USER(9),
    UNFOLLOW_USER(10),
    COMMENT_COMMENT(11),
    DELETE_ANSWER(12),
    DELETE_COMMENT(13);


    private int value;

    EventType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
