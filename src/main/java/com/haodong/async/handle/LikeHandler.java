package com.haodong.async.handle;

import com.haodong.async.EventHandler;
import com.haodong.async.EventModel;
import com.haodong.async.EventType;
import com.haodong.model.Message;
import com.haodong.model.User;
import com.haodong.service.MessageService;
import com.haodong.service.UserService;
import com.haodong.util.WendaUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by torch on 17-3-1.
 */
@Component
public class LikeHandler implements EventHandler{

    @Autowired
    UserService userService;
    @Autowired
    MessageService messageService;

    @Override
    public void doHandler(EventModel eventModel) {
        Message message = new Message();
        //系统通知
        message.setFromId(WendaUtil.SYSTEM_USERID);
        //给谁点的赞
        message.setToId(eventModel.getEntityOwnerId());
        message.setCreatedDate(new Date());
        //触发事件的id
        User user = userService.getUser(eventModel.getActorId());
        message.setContent("用户" + user.getName() + "给你点了赞!");
        messageService.addMessage(message);
    }

    @Override
    public List<EventType> getSupportEventTypes() {
        return Arrays.asList(EventType.LIKE);
    }
}
