package com.haodong.dao;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface LogDAO {

    String TABLE_NAME = "log_record";
    String FIELDS = "biz_id";
    @Insert({"insert into " + TABLE_NAME + " (" + FIELDS + ")values(#{bizId})"})
    int insert(int bizId);
}
