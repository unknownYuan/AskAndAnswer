package com.haodong.dao;

import com.haodong.model.Comment;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface CommentDAO {
    String TABLE_NAME = " comment ";
    String TABLE_FIELDS = " user_id, entity_id, entity_type, content, created_date, status ";
    String STAR = " id," + TABLE_FIELDS;

    /**
     * 增加一条评论
     * @param comment
     */
    @Insert({"insert into " + TABLE_NAME + "(" + TABLE_FIELDS + ")values(#{userId}, #{entityId}, #{entityType}, #{content}, #{createdDate}, #{status})"})
    public int addComment(Comment comment);

    /**
     * 按照当前的时间来排序评论
     * @param entityId
     * @param entityType
     * @return
     */
    @Select({"select " + STAR + " from " + TABLE_NAME + " where entity_id = #{entityId} and entity_type = #{entityType} order by created_date desc"})
    List<Comment> selectCommentByEntity(@Param("entityId") int entityId,
                                        @Param("entityType") int entityType);

    /**
     * 查询评论数
     * @param entityId
     * @param entityType
     * @return
     */
    @Select({"select count(id) from ", TABLE_NAME, " where entity_id = #{entityId} and entity_type = #{entityType}"})
    int getCommentCount(@Param("entityId") int entityId,
                        @Param("entityType") int entityType);

    /**
     * 更新评论的状态
     * @param id
     * @param status
     * @return
     *
     */
    @Update({"update ", TABLE_NAME, " set status = #{status} where id = #{id}"})
    int updateStatus(@Param("id") int id,@Param("status") int status);
}
