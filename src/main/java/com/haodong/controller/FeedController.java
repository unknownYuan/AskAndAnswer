package com.haodong.controller;

import com.haodong.model.Feed;
import com.haodong.model.HostHolder;
import com.haodong.service.FeedService;
import com.haodong.service.FollowService;
import com.haodong.util.EntityType;
import com.haodong.util.JedisAdapter;
import com.haodong.util.RedisKeyGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by torch on 17-3-6.
 */
@Controller
public class FeedController {
    @Autowired
    FeedService feedService;
    @Autowired
    FollowService followService;

    @Autowired
    HostHolder hostHolder;

    @Autowired
    JedisAdapter jedisAdapter;

    /**
     * 拉模式，其实就是从数据库中取出来就可以
     *
     * @param model
     * @return
     */
    @RequestMapping(path = "/pullFeeds", method = RequestMethod.GET)
    private String getPullFeeds(Model model) {
        int localUserId = hostHolder.getUser().getId();
        //首先获取自己的关注对象
        List<Integer> followeeIds = followService.getFollowees(localUserId, EntityType.USER, 0, Integer.MAX_VALUE);
        List<Feed> feeds = feedService.getUserFeeds(Integer.MAX_VALUE, followeeIds, 10);
        model.addAttribute("feeds", feeds);
        return "feeds";
    }

    @RequestMapping(path = "/pushFeeds", method = RequestMethod.GET)
    private String getPushFeeds(Model model) {
        int localUserId = hostHolder.getUser().getId();
        List<String> followeeIds = jedisAdapter.lrange(RedisKeyGenerator.getTimeLineKey(localUserId), 0, 10);
        List<Feed> feeds = new ArrayList<>();
        for (String id :
                followeeIds) {
            Feed feed = feedService.getById(Integer.parseInt(id));
            if (feed == null) {
                continue;
            }
            feeds.add(feed);
        }
        model.addAttribute("feeds", feeds);
        return "feeds";
        //首先获取自己的关注对象,但是这次从特定的推的数据库中取数据
    }
}