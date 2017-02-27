package com.haodong.dao;

import com.haodong.model.Message;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by torch on 17-2-26.
 */
@Mapper
@Repository
public interface MessageDAO {
    String TABLE_NAME = " message ";
    //插入字段不能包含id,因为id是自动生成的
    String INSERT_FIELDS = " from_id, to_id, content, created_date, has_read, conversation_id ";
    String STAR = " id, " + INSERT_FIELDS;

    /**
     * 插入一条信息
     * @param message
     * @return
     */
    @Insert({"insert into ", TABLE_NAME, "(", INSERT_FIELDS, ")values(#{fromId}, #{toId}, #{content}, #{createdDate}, #{hasRead}, #{conversationId})"})
    int addMessage(Message message);

    /**
     * 选出消息列表
     * @param conversationId
     * @param offset
     * @param limit
     * @return
     */
    @Select({"select ", STAR, " from ", TABLE_NAME, " where conversation_id = #{conversationId} order by created_date desc limit #{offset}, #{limit}"})
    List<Message> getConversationDatail(@Param("conversationId") String conversationId,
                                        @Param("offset") int offset,
                                        @Param("limit") int limit);
    @Select({"select ", INSERT_FIELDS, ", count(id) as id from ( select * from ", TABLE_NAME,
    "where from_id = #{userId} or to_id = #{userId} order by created_date desc) tt group by conversation_id order by created_date desc limit #{offset}, #{limit}"})
    List<Message> getConversationList(@Param("userId") int userId,
                                      @Param("offset") int offset,
                                      @Param("limit") int limit);
}
