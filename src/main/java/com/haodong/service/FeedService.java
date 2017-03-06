package com.haodong.service;

import com.haodong.dao.FeedDAO;
import com.haodong.model.Feed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by torch on 17-3-5.
 */
@Service
public class FeedService {
    @Autowired
    FeedDAO feedDAO;

    /**
     * 取得和我相关联的所有人的信息，拉模式
     * @param maxId
     * @param userIds
     * @param count
     * @return
     */

    public List<Feed> getUserFeeds(int maxId, List<Integer> userIds, int count){
        return feedDAO.selectUserFeeds(maxId, userIds, count);
    }

    /**
     * 添加一个feed
     * @param feed
     * @return
     */
    public boolean addFeed(Feed feed){
        return  feedDAO.addFeed(feed) > 0 ? true : false;
    }

    /**
     * 推模式
     * @param id
     * @return
     */
    public Feed getById(int id){
        return feedDAO.getFeedById(id);
    }
}
