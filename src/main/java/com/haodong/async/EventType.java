package com.haodong.async;

/**
 * Created by torch on 17-3-1.
 */
public enum EventType {
    LIKE(0),
    COMMENT(1),
    LOGIN(2),
    MAIL(3);

    private int value;
    EventType(int value){
        this.value = value;
    }
    public int getValue(){
        return value;
    }
}
