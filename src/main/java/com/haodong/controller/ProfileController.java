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

/**
 * Created by torch on 17-3-4.
 */
@Controller
public class ProfileController {
    private static final Logger logger = LoggerFactory.getLogger(ProfileController.class);
    @Autowired
    FollowService followService;
    @Autowired
    HostHolder hostHolder;
    @Autowired
    UserService userService;
    @Autowired
    QuestionService questionService;

    @RequestMapping(path = "/profile", method = RequestMethod.GET)
    public String profile(Model model) {
        if (hostHolder.getUser() == null) {
            return "redirect:/reglogin";
        }
        int userId = hostHolder.getUser().getId();
        String localName = userService.getUser(userId).getName();
        //粉丝数
        long followerCount = followService.getFollowerCount(EntityType.USER, userId);
        model.addAttribute("followerCount", followerCount);

        List<ViewObject> followers = new ArrayList<>();
        List<ViewObject> followees = new ArrayList<>();

        //得到所有的粉丝
        List<User> users = followService.getFollowerUsers(EntityType.USER, userId, 0, 10);
        for (User user :
                users) {
            ViewObject vo = new ViewObject();
            vo.set("username", user.getName());
            followers.add(vo);
        }
        model.addAttribute("followers", followers);

        //得到我关注的所有人
        List<Integer> followeeIds = followService.getFollowees(userId, EntityType.USER, 0, 10);
        for (Integer id :
                followeeIds) {
            User user = userService.getUser(id);
            ViewObject vo = new ViewObject();
            vo.set("username", user.getName());
            followees.add(vo);
        }

        //得到所有我关注的问题
        List<Integer> followedQuestions = followService.getFollowees(userId, EntityType.QUESTION, 0, 10);
        List<ViewObject> questions = new ArrayList<>();
        for (int id :
                followedQuestions) {
            Question q = questionService.queryQuestionById(id);
            if (q != null) {
                ViewObject vo = new ViewObject();
                vo.set("question", q);
                questions.add(vo);
            }else {
                logger.error("问题缺失异常");
            }
        }

        //我关注的用户
        model.addAttribute("followees", followees);
        //我的用户名
        model.addAttribute("localName", localName);
        //我关注的问题
        model.addAttribute("questions", questions);
        return "profile";
    }
}
