package com.haodong.controller;

import com.haodong.async.EventModel;
import com.haodong.async.EventProducer;
import com.haodong.async.EventType;
import com.haodong.model.Comment;
import com.haodong.model.HostHolder;
import com.haodong.model.Question;
import com.haodong.model.User;
import com.haodong.service.CommentService;
import com.haodong.service.QuestionService;
import com.haodong.service.SensitiveService;
import com.haodong.service.UserService;
import com.haodong.util.EntityType;
import com.haodong.util.WendaUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;

/**
 * Created by torch on 17-2-25.
 */
@Controller
public class CommentController {
    @Autowired
    QuestionService questionService;
    @Autowired
    CommentService commentService;
    @Autowired
    HostHolder hostHolder;
    @Autowired
    EventProducer eventProducer;
    @Autowired
    UserService userService;

    @RequestMapping(path = {"/addComment"}, method = RequestMethod.POST)
    public String addComment(@RequestParam("questionId") int questionId,
                             @RequestParam("content") String content){
        int localUserId = hostHolder.getUser().getId();
        Comment comment = new Comment();
        comment.setUserId(localUserId);
        comment.setContent(content);
        comment.setCreatedDate(new Date());
        comment.setStatus(0);//0表示没有被删除
        comment.setEntityId(questionId);
        comment.setEntityType(EntityType.QUESTION);
        commentService.addComment(comment);
        //得到这个问题发布者的id
        int userId  = questionService.queryQuestionById(questionId).getUserId();
        int count = commentService.getComment(questionId, EntityType.QUESTION);

        //产生一条评论事件
        eventProducer.fireEvent(new EventModel(EventType.COMMENT)
                .setActorId(localUserId)
                .setEntityId(questionId)
                .setEntityOwnerId(userId)
                .setEntityType(EntityType.QUESTION));
        //增加完一条评论，更新问题的评论数
        questionService.updateCommentCount(comment.getEntityId(), count);
        //下面的一行是错误的写法
        //return "/question/{questionId}";
        //正确的语法如下
        return "redirect:/question/" + questionId;
    }
}
