package com.haodong.async;

import com.alibaba.fastjson.JSONObject;
import com.haodong.util.JedisAdapter;
import com.haodong.util.RedisKeyGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;

/**
 * Created by torch on 17-3-1.
 */
@Service
public class EventProducer {
    private static final Logger logger = LoggerFactory.getLogger(EventProducer.class);
    @Autowired
    JedisAdapter jedisAdapter;

    /**
     * 生产者将事件转化为JSONObject 对象。放入redis中
     * @param e
     * @return
     */
    public boolean fireEvent(EventModel e){
        try {

            //将对象转换为字符串
            String json = JSONObject.toJSONString(e);
            String key = RedisKeyGenerator.getBizEventqueue();
            return jedisAdapter.lpush(key, json) > 0 ? true :false;
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.error("异步框架出错");
        } finally {

        }
        return false;
    }
}
