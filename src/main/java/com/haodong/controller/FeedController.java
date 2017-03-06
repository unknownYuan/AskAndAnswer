package com.haodong.controller;

import com.haodong.model.Feed;
import com.haodong.model.HostHolder;
import com.haodong.service.FeedService;
import com.haodong.service.FollowService;
import com.haodong.util.EntityType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

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

    /**
     * 拉模式，其实就是从数据库中取出来就可以
     * @param model
     * @return
     */
    @RequestMapping(path = "/pullFeeds", method = RequestMethod.GET)
    private String getPullFeeds(Model model){
        if (hostHolder == null) {
            return "redirect:/reglogin";
        }
        int localUserId = hostHolder.getUser().getId();
        //首先获取自己的关注对象
        List<Integer> followeeIds = followService.getFollowees(localUserId, EntityType.USER, 0, Integer.MAX_VALUE);
        List<Feed> feeds = feedService.getUserFeeds(Integer.MAX_VALUE, followeeIds, 10);
        model.addAttribute("feeds", feeds);
        return "feeds";
    }
}
