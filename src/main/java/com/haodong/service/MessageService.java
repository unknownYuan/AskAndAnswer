package com.haodong.service;

import com.haodong.dao.MessageDAO;
import com.haodong.model.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by torch on 17-2-26.
 */
@Service
public class MessageService {
    @Autowired
    MessageDAO messageDAO;
    @Autowired
    SensitiveService sensitiveService;

    public int addMessage(Message message) {
        message.setContent(sensitiveService.filter(message.getContent()));
        return messageDAO.addMessage(message) > 0 ? message.getId() : 0;
    }

    public List<Message> getConversationDetail(String conversationId) {
        return messageDAO.getConversationDatail(conversationId, 0, 10);
    }


    public List<Message> getConversationList(int userId) {
        return messageDAO.getConversationList(userId, 0, 10);
    }

    public int getConversationUnreadCount(String conversationId, int userId) {
        return messageDAO.getConversationUnreadCount(conversationId, userId);
    }
}
