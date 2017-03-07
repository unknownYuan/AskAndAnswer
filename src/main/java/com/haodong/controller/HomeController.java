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
            //如果是匿名用户,则挑出数据库中最新的10条数据
//            List<Question> list = questionService.getLatestQuestions(0, 0, 100);
//            List<ViewObject> vos = new ArrayList<>();
//            for (int i = 0; i < list.size(); i++) {
//                ViewObject vo = new ViewObject();
//                Question question = list.get(i);
//                vo.set("question", question);
//                //问题和对应的用户应该匹配，不能是随意的id
//                User user = userService.getUser(question.getUserId());
//                vo.set("user", user);
//                //boolean status = followService.isFollower(hostHolder.getUser().getId(), EntityType.USER, user.getId());
////                if (status) {
////                    vo.set("status", "已经关注");
////                } else {
//                    vo.set("status", "没有关注");
////                }
//                vos.add(vo);
//            }
//            model.addAttribute("vos", vos);
//            return "index";
        }
    }

