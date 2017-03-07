package com.haodong.controller;

import com.haodong.async.EventModel;
import com.haodong.async.EventProducer;
import com.haodong.async.EventType;
import com.haodong.model.HostHolder;
import com.haodong.model.Question;
import com.haodong.model.User;
import com.haodong.model.ViewObject;
import com.haodong.service.FollowService;
import com.haodong.service.QuestionService;
import com.haodong.service.UserService;
import com.haodong.util.EntityType;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Mapper
@Controller
public class FollowController {
    @Autowired
    FollowService followService;
    @Autowired
    EventProducer eventProducer;
    @Autowired
    HostHolder hostHolder;
    @Autowired
    QuestionService questionService;
    @Autowired
    UserService userService;

    /**
     * 关注用户
     * @param
     * @return
     */
    @RequestMapping(path = {"/followUser"}, method = RequestMethod.POST)
    public String followUser(@RequestParam("followeeId") int followeeId) {
        int myId = hostHolder.getUser().getId();
        boolean ret = followService.follow(myId, EntityType.USER, followeeId);
        //关注完之后产生事件
        boolean suss = eventProducer.fireEvent(new EventModel(EventType.FOLLOW)
                .setActorId(myId)
                .setEntityId(followeeId)
                .setEntityType(EntityType.USER)
                .setEntityOwnerId(followeeId));
        return "redirect:/";
    }

    /**
     * 取消关注
     * @param userId
     * @return
     */
    @RequestMapping(path = {"/unFollowUser"}, method = RequestMethod.POST)
    public String unFollowUser(@RequestParam("followeeId") int userId) {
        boolean ret = followService.unfollow(hostHolder.getUser().getId(), EntityType.USER, userId);
        //关注完之后产生事件
        eventProducer.fireEvent(new EventModel(EventType.UNFOLLOW)
                .setActorId(hostHolder.getUser().getId())
                .setEntityId(userId)
                .setEntityType(EntityType.USER)
                .setEntityOwnerId(userId));
        return "redirect:/";
    }

    /**
     * 关注问题
     * @param questionId
     * @return
     */
    @RequestMapping(path = {"/followQuestion"}, method = RequestMethod.POST)
    public String followQuestion(@RequestParam("questionId") int questionId) {
        Question q = questionService.queryQuestionById(questionId);
        if (q == null) {
            return "redirect:/";
        }
        boolean ret = followService.follow(hostHolder.getUser().getId(), EntityType.QUESTION, questionId);
        //关注完之后产生事件
        eventProducer.fireEvent(new EventModel(EventType.FOLLOW)
                .setActorId(hostHolder.getUser().getId())
                .setEntityId(questionId)
                .setEntityType(EntityType.QUESTION)
                .setEntityOwnerId(q.getUserId()));
        return "redirect:/";
    }

    /**
     * 取消关注问题
     * @param questionId
     * @return
     */
    @RequestMapping(path = {"/unFollowQuestion"}, method = RequestMethod.POST)
    public String unFollowQuestion(@RequestParam("questionId") int questionId) {
        if (hostHolder.getUser() == null) {
            return "redirect:/reglogin";
        }
        Question q = questionService.queryQuestionById(questionId);
        if (q == null) {
            return "redirect:/";
        }
        boolean ret = followService.unfollow(hostHolder.getUser().getId(), EntityType.QUESTION, questionId);
        //关注完之后产生事件
        eventProducer.fireEvent(new EventModel(EventType.UNFOLLOW)
                .setActorId(hostHolder.getUser().getId())
                .setEntityId(questionId)
                .setEntityType(EntityType.QUESTION)
                .setEntityOwnerId(q.getUserId()));
        return "redirect:/";
    }

    /**
     * 查看我的粉丝
     * @param userId
     * @return
     */
    @RequestMapping(path = {"/user/{uid}/followers"}, method = RequestMethod.GET)
    public String followers(@PathVariable("uid") int userId,
                            Model model){
        //获得粉丝的id列表
        List<Integer> followerIds = followService.getFollowers(EntityType.USER, userId, 0, 10);
        if (hostHolder.getUser() == null) {
            return "redirect:/reglogin";
        }else {
            model.addAttribute("followers", getUsersInfo(hostHolder.getUser().getId(), followerIds));
        }
        return "followers";
    }

    private List<ViewObject> getUsersInfo(int localUserId, List<Integer> ids){
        List<ViewObject> vos  = new ArrayList<>();
        for (Integer id:
             ids) {
            User user = userService.getUser(id);
            ViewObject vo = new ViewObject();
            vo.set("user", user);
            vos.add(vo);
        }
        return vos;
    }

    /**
     * 查看我的关注对象
     * @param userId
     * @return
     */
    @RequestMapping(path = {"/user/{uid}/followees"}, method = RequestMethod.GET)
    public String followees(@PathVariable("uid") int userId,
                            Model model){

        //得到我关注的对象列表
        List<Integer> followeeIds = followService.getFollowees(userId, EntityType.USER, 0, 10);
        if (hostHolder.getUser() == null) {
            return "redirect:/reglogin";
        }else {
            model.addAttribute("followeeIds", getUsersInfo(hostHolder.getUser().getId(), followeeIds));
        }
        return "followees";
    }
}
