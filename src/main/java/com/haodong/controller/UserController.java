package com.haodong.controller;

import com.haodong.model.Feed;
import com.haodong.model.User;
import com.haodong.service.FeedService;
import com.haodong.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by torch on 17-3-8.
 */
@Controller
public class UserController {
    @Autowired
    UserService userService;
    @Autowired
    FeedService feedService;

    @RequestMapping(path = "/user/{userId}")
    private String getUserInfo(Model model,
                               @PathVariable("userId") int userId){
        User user = userService.getUser(userId);
        model.addAttribute("userName", user.getName());
        model.addAttribute("headUrl", user.getHeadUrl());
        List<Integer> lists = new ArrayList<>();
        lists.add(userId);
        List<Feed> feeds = feedService.getUserFeeds(Integer.MAX_VALUE, lists, 10);
        model.addAttribute("feeds", feeds);
//        model.addAttribute("",)
        return "user";
    }
}
