package com.haodong.model;

import com.alibaba.fastjson.JSONObject;

import java.util.Date;

/**
 * Created by torch on 17-3-5.
 */

/**
 * 新鲜事
 */
public class Feed {
    private int id;
    //比如评论和关注是不同的类型
    private int type;
    private int userId;
    private Date createdDate;
    //JSON的格式
    private String data;
    private JSONObject object = null;


    @Override
    public String toString() {
        return "Feed{" +
                "id=" + id +
                ", type=" + type +
                ", userId=" + userId +
                ", createdDate=" + createdDate +
                ", data='" + data + '\'' +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public String getData() {
        return data;

    }

    public void setData(String data) {
        this.data = data;
        object = JSONObject.parseObject(data);
    }
    public String get(String key){
        return object == null ? null : object.getString(key);
    }
}
