package com.haodong.async.handle;

import com.haodong.async.EventHandler;
import com.haodong.async.EventModel;
import com.haodong.async.EventType;
import com.haodong.model.Message;
import com.haodong.model.User;
import com.haodong.service.MessageService;
import com.haodong.service.UserService;
import com.haodong.util.WendaUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * 被人给我点了赞同或者反对
 */
@Component
public class LikeHandler implements EventHandler {
    private static final Logger logger = LoggerFactory.getLogger(LikeHandler.class);
    @Autowired
    UserService userService;
    @Autowired
    MessageService messageService;

    @Override
    public void doHandler(EventModel eventModel) {
        Message message = new Message();
        message.setFromId(eventModel.getActorId());
        //给谁点的赞
        message.setToId(eventModel.getEntityOwnerId());
        message.setCreatedDate(new Date());
        //触发事件的user
        User user = userService.getUser(eventModel.getActorId());
        if (eventModel.getType() == EventType.LIKE) {
            message.setContent("用户" + user.getName() + "赞同了你的评论!");
        } else {
            if (eventModel.getType() == EventType.DISLIKE) {
                message.setContent("用户" + user.getName() + "反对了你的评论！");
            } else {
                logger.error("出现未知类型message异常！");
            }
        }
        messageService.addMessage(message);
    }

    @Override
    public List<EventType> getSupportEventTypes() {
        return Arrays.asList(new EventType[]{EventType.LIKE, EventType.DISLIKE});
    }
}
