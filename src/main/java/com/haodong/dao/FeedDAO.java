package com.haodong.dao;

import com.haodong.model.Feed;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by torch on 17-3-5.
 */
@Mapper
@Repository
public interface FeedDAO {
    String TABLE_NAME = " feed ";
    String INSERT_FIELDS = " user_id, data, created_date, type ";
    String STAR = " id, " + INSERT_FIELDS;

    /**
     * 插入
     * @param feed
     * @return
     */
    @Insert({"insert into ", TABLE_NAME, INSERT_FIELDS, "values(#{userId}, #{data}, #{createdDate}, #{type})"})
    int addFeed(Feed feed);

    /**
     * 拉模式，取动态
     * @param maxId
     * @param userIds
     * @param count
     * @return
     */

    List<Feed> selectUserFeeds(@Param("maxId") int maxId,
                               @Param("userIds") List<Integer> userIds,
                               @Param("count") int count);

    /**
     * 选择推模式
     * @param id
     * @return
     */
    @Select({"select ", STAR, " from ", TABLE_NAME, " where id = #{id}"})
    Feed getFeedById(@Param("id") int id);

}
