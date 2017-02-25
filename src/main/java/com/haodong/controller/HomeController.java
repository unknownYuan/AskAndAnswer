package com.haodong.controller;

import com.haodong.model.Question;
import com.haodong.model.ViewObject;
import com.haodong.service.QuestionService;
import com.haodong.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by h on 17-2-20.
 */

@Controller
public class HomeController {
    private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

    @Autowired
    QuestionService questionService;
    @Autowired
    UserService userService;

    @RequestMapping(path = {"/", "/index", "/home"}, method = RequestMethod.GET)
    public String index(Model model) {
        List<Question> list = questionService.getLatestQuestions(0, 0, 10);
        List<ViewObject> vos = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            ViewObject vo = new ViewObject();
            Question question = list.get(i);
            vo.set("question", question);
            //问题和对应的用户应该匹配，不能是随意的id
            vo.set("user", userService.getUser(question.getUserId()));
            vos.add(vo);
        }
        model.addAttribute("vos", vos);
        return "index";
    }

    @RequestMapping(path = "/question/{questionId}")
    public String getQuestion(Model model,
                              @PathVariable("questionId") int questionId) {
        Question question = questionService.queryQuestionById(questionId);
        model.addAttribute("question", question);
        return "question";
    }
}
