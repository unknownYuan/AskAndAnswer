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
import org.apache.ibatis.annotations.Param;
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

//    @RequestMapping(path = "/commentComment", method = RequestMethod.POST)
//    public String commentComment(@RequestParam("commentId"),
//                                 @RequestParam("content")){
//
//    }

    @RequestMapping(path = {"/addComment"}, method = RequestMethod.POST)
    public String addComment(@RequestParam("id") int id,
                             @RequestParam("content") String content,
                             @RequestParam("entityType") int entityType){
        int localUserId = hostHolder.getUser().getId();

        Comment comment = new Comment();
        comment.setUserId(localUserId);
        comment.setContent(content);
        comment.setCreatedDate(new Date());
        comment.setStatus(0);//0表示没有被删除
        comment.setEntityId(id);

        if(entityType == EntityType.QUESTION) {
            comment.setEntityType(EntityType.QUESTION);
            commentService.addComment(comment);
            //得到这个问题发布者的id

            int userId  = questionService.queryQuestionById(id).getUserId();
            int count = commentService.getComment(id, EntityType.QUESTION);

            //产生一条评论事件
            eventProducer.fireEvent(new EventModel(EventType.COMMENT_QUESTION)
                    .setActorId(localUserId)
                    .setEntityId(id)
                    .setEntityOwnerId(userId)
                    .setEntityType(EntityType.QUESTION));
            //增加完一条评论，更新问题的评论数
            questionService.updateCommentCount(comment.getEntityId(), count);
            //下面的一行是错误的写法
            //return "/question/{questionId}";
            //正确的语法如下
            return "redirect:/question/" + id;
        }else {
            comment.setEntityType(EntityType.COMMENT);
            commentService.addComment(comment);
            //得到这个问题发布者的id

            Comment c = commentService.getCommentById(id);
            int userId = c.getUserId();
            //int count = commentService.getComment(id, EntityType.COMMENT);

            //产生一条评论事件
            eventProducer.fireEvent(new EventModel(EventType.COMMENT_COMMENT)
                    .setActorId(localUserId)
                    .setEntityId(id)
                    .setEntityOwnerId(userId)
                    .setEntityType(EntityType.COMMENT));
            //增加完一条评论，更新问题的评论数
            //questionService.updateCommentCount(comment.getEntityId(), count);
            //下面的一行是错误的写法
            //return "/question/{questionId}";
            //正确的语法如下
            return "redirect:/question/" + c.getEntityId();
        }


    }
}
