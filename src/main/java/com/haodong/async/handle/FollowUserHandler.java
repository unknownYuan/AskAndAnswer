package com.haodong.async.handle;

import com.haodong.async.EventHandler;
import com.haodong.async.EventModel;
import com.haodong.async.EventType;
import com.haodong.model.Message;
import com.haodong.model.User;
import com.haodong.service.MessageService;
import com.haodong.service.UserService;
import com.haodong.util.EntityType;
import com.haodong.util.WendaUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Component
public class FollowUserHandler implements EventHandler{

    private static final Logger logger = LoggerFactory.getLogger(FollowUserHandler.class);
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
        if(eventModel.getEntityType() == EntityType.USER){
            message.setContent("用户" + user.getName() + "最近关注了你！");
        }else {
            logger.error("事件类型异常");
        }
        messageService.addMessage(message);
    }

    @Override
    public List<EventType> getSupportEventTypes() {
        return Arrays.asList(EventType.FOLLOW_USER);
    }
}
