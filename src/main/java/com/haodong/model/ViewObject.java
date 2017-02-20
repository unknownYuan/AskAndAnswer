package com.haodong.model;

import org.apache.commons.collections.map.HashedMap;

import java.util.Map;


/**
 * Created by h on 17-2-20.
 */
public class ViewObject {
    private Map<String, Object> objs = new HashedMap();

    public void set(String key, Object value) {
        objs.put(key, value);
    }

    public Object get(String key) {
        return objs.get(key);
    }
}
