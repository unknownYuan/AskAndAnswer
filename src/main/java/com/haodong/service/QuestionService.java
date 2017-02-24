package com.haodong.service;

import com.haodong.dao.QuestionDAO;
import com.haodong.dao.UserDAO;
import com.haodong.model.Question;
import com.haodong.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuestionService {
    @Autowired
    QuestionDAO questionDAO;


    public int addQuestion(Question question) {
        //敏感词过滤
        return questionDAO.addQuestion(question) > 0 ? question.getId() : 0;
    }


    /**
     * 得到最新的几条数据
     */
    public List<Question> getLatestQuestions(int userId, int offset, int limit) {
        return questionDAO.selectLatestQuestions(userId, offset, limit);
    }
}
