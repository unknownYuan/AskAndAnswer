package com.haodong;

import com.fasterxml.jackson.databind.annotation.JsonAppend;
import com.haodong.async.EventModel;
import com.haodong.async.EventProducer;
import com.haodong.async.EventType;
import com.haodong.dao.QuestionDAO;
import com.haodong.dao.UserDAO;
import com.haodong.model.Question;
import com.haodong.model.User;
import com.haodong.service.FollowService;
import com.haodong.service.QuestionService;
import com.haodong.service.UserService;
import com.haodong.util.EntityType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.servlet.http.Cookie;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Created by h on 17-2-19.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = MyWendaApplication.class)
@Sql("/init-schema.sql")
public class InitDatabaseTests {
    @Autowired
    UserDAO userDAO;
    @Autowired
    QuestionDAO questionDAO;
    @Autowired
    UserService userService;
    @Autowired
    QuestionService questionService;
    @Autowired
    FollowService followService;
    @Autowired
    EventProducer eventProducer;
    @Test
    public void initDatabase() {

        Random random = new Random();
        //可能存在游客，所以需要有一个匿名用户
        userService.register("游客", "root");
        //为User表插入数据
        for (int i = 0; i < 20; i++) {
            String password = "root";
            userService.register("userName" + (i+1), password);
        }


        //userDAO.updatePassword(user);
        //为question表插入数据
        for (int i = 0; i < 20; i++) {
            Question question = new Question();
            question.setContent("this is content!");
            question.setCommentCount(i + new Random().nextInt(100));
            question.setCreatedDate(new Date(System.currentTimeMillis() + i * 3600));
            //用户的id
            int userId = new Random().nextInt(10) + 1;
            question.setUserId(userId);
            question.setTitle("title number " + i);
            questionDAO.addQuestion(question);

            boolean ret = followService.follow(userId, EntityType.QUESTION, i+1);
            //关注完之后产生事件
            eventProducer.fireEvent(new EventModel(EventType.FOLLOW_QUESTION)
                    .setActorId(userId)
                    .setEntityId(i+1)
                    .setEntityType(EntityType.QUESTION)
                    .setEntityOwnerId(userId));
        }
        List<Question> ls = questionDAO.selectLatestQuestions(0, 0, 10);
        //userId 为0的时候会选出10条语句，如果是其他数字，只能选出一条语句
        System.out.println("------------------");
        for (int i = 0; i < ls.size(); i++) {
            System.out.println(ls.get(i).getUserId());
        }
        System.out.println("------------------");
    }
//        userDAO.deleteById(1);
//        userDAO.deleteById(6);
//        userDAO.queryById(3);
}

