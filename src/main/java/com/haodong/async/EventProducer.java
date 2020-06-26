package com.haodong.async;

import com.google.gson.Gson;
import com.haodong.controller.LoginController;
import com.haodong.util.RedisKeyUtil;
import com.haodong.util.RedisssionCluster;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class EventProducer {
    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);
    @Autowired
    RedisssionCluster redisssionCluster;
    @Autowired
    Gson gson;

    public boolean fireEvent(EventModel eventModel) {
        try {
            String json = gson.toJson(eventModel);
            String key = RedisKeyUtil.getEventQueueKey();
            redisssionCluster.lpush(key, json);
            return true;
        } catch (Exception e) {
            logger.error("持久化数据出错...");
            return false;
        }
    }
}
