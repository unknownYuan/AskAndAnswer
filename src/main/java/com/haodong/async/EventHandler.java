package com.haodong.async;

import java.util.List;

/**
 * Created by haodong on 0.
 */
public interface EventHandler {
    void doHandle(EventModel model);

    List<EventType> getSupportEventTypes();
}
