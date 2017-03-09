package com.haodong.async.handle;

import com.alibaba.fastjson.JSONObject;
import com.haodong.async.EventHandler;
import com.haodong.async.EventModel;
import com.haodong.async.EventType;
import com.haodong.model.Feed;
import com.haodong.model.Message;
import com.haodong.model.Question;
import com.haodong.model.User;
import com.haodong.service.*;
import com.haodong.util.EntityType;
import com.haodong.util.JedisAdapter;
import com.haodong.util.RedisKeyGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

import java.util.*;

/**
 * Created by torch on 17-3-9.
 * 我回答了问题，我的粉丝时间线上就会出现我的动态
 */
@Component
public class CommentQuestionHandler implements EventHandler {
    private static final Logger logger = LoggerFactory.getLogger(CommentQuestionHandler.class);
    @Autowired
    JedisAdapter jedisAdapter;
    @Autowired
    FollowService followService;
    @Autowired
    UserService userService;
    @Autowired
    QuestionService questionService;
    @Autowired
    FeedService feedService;
    @Autowired
    MessageService messageService;

    private String buildFeedData(EventModel model) {
        Map<String, String> map = new HashMap<>();
        User actor = userService.getUser(model.getActorId());
        map.put("userHead", actor.getHeadUrl());//谁回答了问题
        map.put("userName", actor.getName());//他的用户名是什么
        if (model.getType() == EventType.COMMENT_QUESTION) {
            Question question = questionService.queryQuestionById(model.getEntityId());
            map.put("questionId", String.valueOf(question.getId()));//关注问题的id是什么
            map.put("questionTitle", String.valueOf(question.getTitle()));//关注的问题的标题是什么
            return JSONObject.toJSONString(map);
        }
        return null;
    }

    @Override
    public void doHandler(EventModel eventModel) {
        //产生一条新的动态
        Feed feed = new Feed();
        feed.setCreatedDate(new Date());
        feed.setUserId(eventModel.getActorId());
        feed.setType(eventModel.getType().getValue());
        feed.setData(buildFeedData(eventModel));
        //添加到mysql数据库中，享元模式
        feedService.addFeed(feed);
        //将我回答的问题推送给我的粉丝
        List<Integer> followers = followService.getFollowers(EntityType.USER, eventModel.getActorId(), Integer.MAX_VALUE);
        for (int follower :
                followers) {
            String timeLineKey = RedisKeyGenerator.getTimeLineKey(follower);
            //添加到redis中，支持push
            jedisAdapter.lpush(timeLineKey, String.valueOf(feed.getId()));
        }
        //将我回答的问题当做消息发送给关注这个问题的人
        int questionId = eventModel.getEntityId();
        List<Integer> questionIds = followService.getFollowers(EntityType.QUESTION, questionId, 0, Integer.MAX_VALUE);
        for (int followerId:
             questionIds) {
            Message message = new Message();
            message.setFromId(eventModel.getActorId());
            message.setToId(followerId);//将消息发送给关注这个问题的每个人
            message.setContent("用户回答了你关注的问题");
            message.setCreatedDate(new Date());
            message.setHasRead(0);
            message.setConversationId("1_1");
            messageService.addMessage(message);
            String commentQuestionKey = RedisKeyGenerator.getCommentQuestionKey(followerId);
            jedisAdapter.lpush(commentQuestionKey, String.valueOf(message.getId()));
        }
    }

    @Override
    public List<EventType> getSupportEventTypes() {
        return Arrays.asList(new EventType[]{EventType.COMMENT_QUESTION});
    }
}
