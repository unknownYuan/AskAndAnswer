package com.haodong.dao;

import com.haodong.model.User;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

/**
 * Created by h on 17-2-19.
 */
@Repository
@Mapper
public interface UserDAO {
    String TABLE_NAME = " user ";
    String INSERT_FIELDS = " name, password, salt, head_url ";
    String STAR = " id, " + INSERT_FIELDS;

    /**
     * 使用注解的方式插入数据
     *
     * @param user
     * @return
     */
    @Insert({"insert into ", TABLE_NAME, "(", INSERT_FIELDS,
            ") values(#{name}, #{password}, #{salt}, #{headUrl})"})
    int addUser(User user);

    /**
     * 注解的方式查询语句
     *
     * @param id
     * @return
     */
    @Select({"select ", INSERT_FIELDS, " from ", TABLE_NAME, " where id = #{id}"})
    User queryById(int id);

    @Select({"select ", STAR, " from ", TABLE_NAME, " where name = #{name}"})
    User queryByName(String name);
    /**
     * 使用注解更新语句
     *
     * @param user
     */
    @Update({"update ", TABLE_NAME, " set password = #{password} where id = #{id}"})
    void updatePassword(User user);

    /**
     * 通过注解的方式删除记录
     *
     * @param id
     */
    @Delete({"delete from ", TABLE_NAME, " where id = #{id}"})
    void deleteById(int id);
}
