package com.haodong.controller;

import com.haodong.model.HostHolder;
import com.haodong.model.Question;
import com.haodong.service.QuestionService;
import com.haodong.util.WendaUtil;
import org.apache.log4j.Logger;
import org.apache.log4j.spi.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

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


    @RequestMapping(path = "/question/add", method = {RequestMethod.POST})
    @ResponseBody //字符串是我们生成的，不需要返回一个html页面
    public String addQuestion(@RequestParam(value = "title") String title,
                              @RequestParam(value = "content") String content) {
        Question question = new Question();
        //用户提供的内容
        question.setContent(content);
        question.setTitle(title);
        //自己添加字段
        question.setCreatedDate(new Date());
        if (hostHolder.getUser() == null) {
            question.setUserId(WendaUtil.ANONYMOUS_USERID);
        } else {
            question.setUserId(hostHolder.getUser().getId());
        }
        question.setCommentCount(0);
        //如果添加问题成功
        if (questionService.addQuestion(question) > 0) {
            return WendaUtil.getJSONString(0, "成功");
        }
        //如果添加问题失败
        return WendaUtil.getJSONString(1, "失败");
    }
}
