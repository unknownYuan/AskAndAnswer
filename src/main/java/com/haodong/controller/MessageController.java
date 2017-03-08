package com.haodong.controller;

import com.haodong.model.HostHolder;
import com.haodong.model.Message;
import com.haodong.model.User;
import com.haodong.model.ViewObject;
import com.haodong.service.MessageService;
import com.haodong.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.swing.text.View;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
public class MessageController {
    @Autowired
    MessageService messageService;
    @Autowired
    HostHolder hostHolder;
    @Autowired
    UserService userService;

    @RequestMapping(path = {"/msg/notification"}, method = RequestMethod.GET)
    public String notificate(Model model) {
        int localUserId = hostHolder.getUser().getId();
        List<Message> messages = messageService.getConversationList(localUserId);
        List<ViewObject> vos = new ArrayList<>();
        for (Message m:
             messages) {
            ViewObject vo = new ViewObject();
            vo.set("message", m);
            vos.add(vo);
        }
        model.addAttribute("vos", vos);
        return "notification";
    }

    @RequestMapping(path = "/Msg/addMessage", method = RequestMethod.POST)
    public String addMessage(@RequestParam("toName") String toName,
                             @RequestParam("content") String content) {

        if (hostHolder.getUser() == null) {
            return "redirect:/reglogin";
        } else {
            User user = userService.getUserByName(toName);
            if (user == null) {
                //return WendaUtil.getJSONString(1, "用户不存在！！");
                return "redirect:/";
            } else {
                Message message = new Message();
                message.setCreatedDate(new Date());
                message.setContent(content);
                message.setFromId(hostHolder.getUser().getId());
                message.setToId(user.getId());
                messageService.addMessage(message);
                //return WendaUtil.getJSONString(0, "成功！！");
                return "redirect:/";
            }
        }
    }

    @RequestMapping(path = "/msg/list", method = RequestMethod.GET)
    public String getConversationList(Model model) {
        if (hostHolder.getUser() == null) {
            return "redirect:/reglogin";
        } else {
            int localUserId = hostHolder.getUser().getId();
            List<Message> messages = messageService.getConversationList(localUserId);
            List<ViewObject> vos = new ArrayList<>();
            for (Message message : messages) {
                ViewObject vo = new ViewObject();
                vo.set("conversation", message);
                int targetId = message.getFromId() == localUserId ? message.getToId() : message.getFromId();
                vo.set("target", userService.getUser(targetId));
                int unreadCount = messageService.getConversationUnreadCount(message.getConversationId(), localUserId);
                vo.set("unread", unreadCount);
                vos.add(vo);
            }
            model.addAttribute("vos", vos);
            //vos被传入到letter页面
            return "letter";
        }
    }

    @RequestMapping(path = "/msg/detail", method = RequestMethod.GET)
    public String getConversationDetail(Model model,
                                        @RequestParam("conversationId") String conversationId) {
        List<Message> messages = messageService.getConversationDetail(conversationId);
        List<ViewObject> viewObjects = new ArrayList<>();
        for (int i = 0; i < messages.size(); i++) {
            Message message = messages.get(i);
            ViewObject viewObject = new ViewObject();
            viewObject.set("message", message);
            viewObject.set("user", userService.getUser(message.getFromId()));
            viewObjects.add(viewObject);
        }
        model.addAttribute("vos", viewObjects);
        return "letterDetail";
    }
}