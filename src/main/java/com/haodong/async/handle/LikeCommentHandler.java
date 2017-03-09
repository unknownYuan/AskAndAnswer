package com.haodong.async.handle;

import com.haodong.async.EventHandler;
import com.haodong.async.EventModel;
import com.haodong.async.EventType;
import com.haodong.model.Message;
import com.haodong.model.User;
import com.haodong.service.MessageService;
import com.haodong.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Component
public class LikeCommentHandler implements EventHandler {
    private static final Logger logger = LoggerFactory.getLogger(LikeCommentHandler.class);
    @Autowired
    UserService userService;
    @Autowired
    MessageService messageService;

    @Override
    public void doHandler(EventModel eventModel) {
        Message message = new Message();
        message.setFromId(eventModel.getActorId());
        message.setToId(eventModel.getEntityOwnerId());
        message.setCreatedDate(new Date());
        User user = userService.getUser(eventModel.getActorId());
        if (eventModel.getType() == EventType.LIKE_COMMENT) {
            message.setContent("用户" + user.getName() + "赞同了XXX在XXX问题下的评论!");
        } else {
            if (eventModel.getType() == EventType.DISLIKE_COMMENT) {
                message.setContent("用户" + user.getName() + "反对了XXX在XXX问题下的评论！");
            } else {
                logger.error("未知类型异常！");
            }
        }
        messageService.addMessage(message);

    }

    @Override
    public List<EventType> getSupportEventTypes() {
        return Arrays.asList(new EventType[]{EventType.LIKE_COMMENT, EventType.DISLIKE_COMMENT});
    }
}
