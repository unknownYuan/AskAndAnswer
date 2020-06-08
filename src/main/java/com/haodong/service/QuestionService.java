package com.haodong.service;

import com.haodong.dao.QuestionDAO;
import com.haodong.model.Question;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.HtmlUtils;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Random;

@Service
public class QuestionService {
    @Autowired
    QuestionDAO questionDAO;

    @Autowired
    SensitiveService sensitiveService;

    @Autowired
    LogService logService;

    @Autowired
    private PlatformTransactionManager ptm;

    @PostConstruct
    public void init() {
        System.out.println("tm="+ptm);
    }


    public Question getById(int id) {
        return questionDAO.getById(id);
    }

    @Transactional
    public int addQuestion(Question question) {
        question.setTitle(HtmlUtils.htmlEscape(question.getTitle()));
        question.setContent(HtmlUtils.htmlEscape(question.getContent()));
        // 敏感词过滤
        question.setTitle(sensitiveService.filter(question.getTitle()));
        question.setContent(sensitiveService.filter(question.getContent()));
        int result = logService.record(question.getId());
        int success =  questionDAO.addQuestion(question) > 0 ? question.getId() : 0;
        System.out.println("test success = " + success + "  result =" +result);
        return success;
    }

    public List<Question> getLatestQuestions(int userId, int offset, int limit) {
        return questionDAO.selectLatestQuestions(userId, offset, limit);
    }

    public int updateCommentCount(int id, int count) {

        return questionDAO.updateCommentCount(id, count);
    }
}
