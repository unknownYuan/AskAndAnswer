package com.haodong.async;

import com.alibaba.fastjson.JSONObject;
import com.haodong.util.JedisAdapter;
import com.haodong.util.RedisKeyGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by torch on 17-3-1.
 */
@Service
public class EventProducer {
    @Autowired
    JedisAdapter jedisAdapter;

    public boolean fireEvent(EventModel e){
        try {
            //将对象转换为字符串
            String json = JSONObject.toJSONString(e);
            String key = RedisKeyGenerator.getBizEventqueue();
            jedisAdapter.lpush(key, json);
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        } finally {

        }
    }
}
