package com.haodong.service;

import com.haodong.dao.QuestionDAO;
import com.haodong.dao.UserDAO;
import com.haodong.model.Question;
import com.haodong.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

@Service
public class QuestionService {
    @Autowired
    QuestionDAO questionDAO;

    @Autowired
    SensitiveService sensitiveService;
    /**
     * 添加问题
     * @param question
     * @return
     */
    public int addQuestion(Question question) {
        //敏感词过滤
        question.setTitle(HtmlUtils.htmlEscape(question.getTitle()));
        question.setContent(HtmlUtils.htmlEscape(question.getContent()));

        question.setContent(sensitiveService.filter(question.getContent()));
        question.setTitle(sensitiveService.filter(question.getTitle()));
        return questionDAO.addQuestion(question) > 0 ? question.getId() : 0;
    }

    public Question queryQuestionById(int id){
        return questionDAO.queryQuestionById(id);
    }

    /**
     * 得到最新的几条数据
     */
    public List<Question> getLatestQuestions(int userId, int offset, int limit) {
        return questionDAO.selectLatestQuestions(userId, offset, limit);
    }

    public void updateCommentCount(int entityId, int count) {
        questionDAO.updateCommentCount(entityId, count);
    }
}
