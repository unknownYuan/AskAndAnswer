package com.haodong.async;

import java.util.List;

/**
 * Created by torch on 17-3-1.
 */
public interface EventHandler {
    //处理事件
    void doHandler(EventModel eventModel);
    //注册事件
    List<EventType> getSupportEventTypes();
}
