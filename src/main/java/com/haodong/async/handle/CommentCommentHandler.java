package com.haodong.async.handle;

import com.haodong.async.EventHandler;
import com.haodong.async.EventModel;
import com.haodong.async.EventType;
import com.haodong.model.Message;
import com.haodong.service.*;
import com.haodong.util.JedisAdapter;
import com.haodong.util.RedisKeyGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class CommentCommentHandler implements EventHandler {
    private static final Logger logger = LoggerFactory.getLogger(CommentCommentHandler.class);
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

    @Override
    public void doHandler(EventModel eventModel) {
        Message message = new Message();
        message.setFromId(eventModel.getActorId());
        message.setToId(eventModel.getEntityOwnerId());
        message.setContent("用户评论了你在。。。问题下的评论");
        message.setCreatedDate(new Date());
        message.setHasRead(0);
        messageService.addMessage(message);
        String commentCommentKey = RedisKeyGenerator.getCommentCommentKey(eventModel.getEntityOwnerId());
        jedisAdapter.lpush(commentCommentKey, String.valueOf(message.getId()));
    }

    @Override
    public List<EventType> getSupportEventTypes() {
        return Arrays.asList(new EventType[]{EventType.COMMENT_COMMENT});
    }
}
