package com.haodong.controller;

import com.haodong.model.Comment;
import com.haodong.model.HostHolder;
import com.haodong.model.Question;
import com.haodong.model.User;
import com.haodong.service.CommentService;
import com.haodong.service.QuestionService;
import com.haodong.service.SensitiveService;
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

    @RequestMapping(path = {"/addComment"}, method = RequestMethod.POST)
    public String addComment(@RequestParam("questionId") int questionId,
                             @RequestParam("content") String content){
        Comment comment = new Comment();
        comment.setContent(content);
        comment.setCreatedDate(new Date());
        comment.setStatus(0);
        //userId 由网站的拦截器获取
//        User user = hostHolder.getUser();
//        System.out.println("-----------------");
//        System.out.println(user.getId());
//        System.out.println(user.getName());
//        System.out.println(user.getHeadUrl());
//        System.out.println("-----------------");
        if(hostHolder.getUser().getId() != 0) {
            comment.setUserId(hostHolder.getUser().getId());
        }else {
            //设置匿名用户
            comment.setUserId(WendaUtil.ANONYMOUS_USERID);
        }
        comment.setEntityId(questionId);
        comment.setEntityType(EntityType.QUESTION_TYPE);
        commentService.addComment(comment);
        int count = commentService.getComment(questionId, EntityType.QUESTION_TYPE);
        System.out.println("---------------------");
        System.out.println(count);
        System.out.println("---------------------");
        //增加完一条评论，更新问题的评论数
        questionService.updateCommentCount(comment.getEntityId(), count);
        //下面的一行是错误的写法
        //return "/question/{questionId}";
        //正确的语法如下
        return "/question/" + questionId;
    }
}
