package com.haodong.controller;

import com.haodong.model.Comment;
import com.haodong.model.HostHolder;
import com.haodong.model.Question;
import com.haodong.model.ViewObject;
import com.haodong.service.CommentService;
import com.haodong.service.LikeService;
import com.haodong.service.QuestionService;
import com.haodong.service.UserService;
import com.haodong.util.EntityType;
import com.haodong.util.WendaUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by torch on 17-2-22.
 */
@Controller
public class QuestionController {
    @Autowired
    QuestionService questionService;

    @Autowired
    HostHolder hostHolder;

    private static org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(QuestionController.class);


    /**
     *
     * @param model
     * @param questionId
     * @return
     */
    /**
     * this is question detail
     *
     * @param model
     * @param questionId
     * @return
     */
    @Autowired
    CommentService commentService;
    @Autowired
    UserService userService;
    @Autowired
    LikeService likeService;

    @RequestMapping(path = "/question/{questionId}")
    public String getQuestion(Model model,
                              @PathVariable("questionId") int questionId) {
        Question question = questionService.queryQuestionById(questionId);
        //添加问题到model
        model.addAttribute("question", question);
        logger.info("title", question.getTitle());
        //添加这个问题的评论到model
        List<Comment> comments = commentService.getCommentByEntity(questionId, EntityType.QUESTION_TYPE);
        List<ViewObject> vos = new ArrayList<>();
        for (Comment comment:
             comments) {
            ViewObject vo = new ViewObject();
            //这条评论：$!{vos.vo.comment}
            vo.set("comment", comment);
            //这条评论的赞数$!{vos.vo.likeCount}
            long cnt = likeService.getLikeCount(EntityType.COMMENT_TYPE, comment.getId());
            vo.set("likeCount", cnt);
            //这条评论的作者 $!{vos.vo.user}
            vo.set("user", userService.getUser(comment.getUserId()));
            vos.add(vo);
        }
        model.addAttribute("vos", vos);
        return "question";
    }

    @RequestMapping(path = "/question/add", method = {RequestMethod.POST})
    //@ResponseBody //字符串是我们生成的，不需要返回一个html页面
    public String addQuestion(@RequestParam(value = "title") String title,
                              @RequestParam(value = "content") String content) {
        Question question = new Question();
        //用户提供的内容
        question.setContent(content);
        question.setTitle(title);
        //自己添加字段
        question.setCreatedDate(new Date());
        if (hostHolder.getUser() == null) {
            //不允许匿名用户添加问题,跳到登陆注册页面
            return "redirect:/";
        } else {
            question.setUserId(hostHolder.getUser().getId());
        }
        question.setCommentCount(0);
        //如果添加问题成功
        if (questionService.addQuestion(question) > 0) {

            String res = WendaUtil.getJSONString(0, "添加问题成功");
            System.out.println(res);
            return "redirect:/";
        }
        //如果添加问题失败
        String res = WendaUtil.getJSONString(1, "添加问题失败");
        System.out.println(res);
        return "redirect:/";
    }
}
