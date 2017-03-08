package com.haodong.controller;

import com.haodong.model.HostHolder;
import com.haodong.model.Question;
import com.haodong.model.User;
import com.haodong.model.ViewObject;
import com.haodong.service.FollowService;
import com.haodong.service.QuestionService;
import com.haodong.service.UserService;
import com.haodong.util.EntityType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;
import java.util.List;


@Controller
public class HomeController {
    private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

    @Autowired
    QuestionService questionService;
    @Autowired
    UserService userService;
    @Autowired
    FollowService followService;
    @Autowired
    HostHolder hostHolder;

    @RequestMapping(path = {"/", "/index", "/home"}, method = RequestMethod.GET)
    public String index(Model model) {
        //显示timeLine
        return "redirect:/pullFeeds";
    }
}

