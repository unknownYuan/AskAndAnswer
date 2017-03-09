package com.haodong.async.handle;

import com.alibaba.fastjson.JSONObject;
import com.haodong.async.EventHandler;
import com.haodong.async.EventModel;
import com.haodong.async.EventType;
import com.haodong.model.Feed;
import com.haodong.model.Message;
import com.haodong.model.User;
import com.haodong.service.FeedService;
import com.haodong.service.FollowService;
import com.haodong.service.MessageService;
import com.haodong.service.UserService;
import com.haodong.util.EntityType;
import com.haodong.util.JedisAdapter;
import com.haodong.util.RedisKeyGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class LikeAnswerHandler implements EventHandler {
    private static final Logger logger = LoggerFactory.getLogger(LikeAnswerHandler.class);
    @Autowired
    UserService userService;
    @Autowired
    MessageService messageService;
    @Autowired
    FollowService followService;

    @Autowired
    FeedService feedService;
    @Autowired
    JedisAdapter jedisAdapter;
    @Override
    public void doHandler(EventModel eventModel) {
        Feed feed = new Feed();
        feed.setCreatedDate(new Date());
        feed.setType(eventModel.getType().getValue());
        feed.setUserId(eventModel.getActorId());
        feed.setData(buildFeedData(eventModel));
        feedService.addFeed(feed);
        List<Integer> followerIds = followService.getFollowers(EntityType.USER, eventModel.getActorId(), Integer.MAX_VALUE);
        for (int id:
             followerIds) {
            String key = RedisKeyGenerator.getTimeLineKey(id);
            jedisAdapter.lpush(key, String.valueOf(feed.getId()));
        }
    }

    private String buildFeedData(EventModel eventModel) {
        Map<String, String> maps = new HashMap<>();
        int questionId = eventModel.getEntityId();
        maps.put("questionId", String.valueOf(questionId));
        return JSONObject.toJSONString(maps);
    }

    @Override
    public List<EventType> getSupportEventTypes() {
        return Arrays.asList(new EventType[]{EventType.LIKE_ANSWER, EventType.DISLIKE_ANSWER});
    }
}
