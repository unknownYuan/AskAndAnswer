package com.haodong.async;

import com.alibaba.fastjson.JSON;
import com.haodong.util.JedisAdapter;
import com.haodong.util.RedisKeyGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by torch on 17-3-1.
 */
@Service
public class EventConsumer implements InitializingBean, ApplicationContextAware {
    private static final Logger logger = LoggerFactory.getLogger(EventConsumer.class);
    private Map<EventType, List<EventHandler>> config = new HashMap<>();
    private ApplicationContext applicationContext;
    @Autowired
    JedisAdapter jedisAdapter;

    @Override
    public void afterPropertiesSet() throws Exception {
        //得到所有EventHandler的实现类
        Map<String, EventHandler> beans = applicationContext.getBeansOfType(EventHandler.class);

        if (beans != null) {
            for (Map.Entry<String, EventHandler> entry :
                    beans.entrySet()) {
                List<EventType> eventTypeList = entry.getValue().getSupportEventTypes();
                for (EventType eventType :
                        eventTypeList) {
                    if (!config.containsKey(eventType)) {
                        config.put(eventType, new ArrayList<EventHandler>());
                    }
                    config.get(eventType).add(entry.getValue());
                }
            }
        }

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    String key = RedisKeyGenerator.getBizEventqueue();
                    List<String> events = jedisAdapter.brpop(0, key);
                    if (events != null) {
                        for (String message :
                                events) {
                            if (message.equals(key)) {
                                continue;
                            }
                            EventModel eventModel = JSON.parseObject(message, EventModel.class);
                            if (!config.containsKey(eventModel.getType())) {
                                logger.error("不能识别的事件类型");
                                continue;
                            }
                            for (EventHandler eventHandler :
                                    config.get(eventModel.getType())) {
                                eventHandler.doHandler(eventModel);
                            }
                        }
                    }else {
                        System.out.println("事件为空");
                    }
                }
            }
        });
        thread.start();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
