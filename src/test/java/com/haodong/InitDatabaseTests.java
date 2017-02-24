package com.haodong;

import com.haodong.dao.QuestionDAO;
import com.haodong.dao.UserDAO;
import com.haodong.model.Question;
import com.haodong.model.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;
import java.util.List;
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

    @Test
    public void initDatabase() {
        Random random = new Random();
        //为User表插入数据
        for (int i = 0; i < 11; i++) {
            User user = new User();
            user.setHeadUrl(String.format("http://images.nowcoder.com/head/%dt.png", random.nextInt(1000)));
            user.setName(String.format("USER%d", 1000 + i));//注意：用户不能有重复
            user.setPassword("password2");
            user.setSalt("salt");
            userDAO.addUser(user);
        }
        //userDAO.updatePassword(user);
        //为question表插入数据
        for (int i = 0; i < 11; i++) {
            Question question = new Question();
            question.setContent("this is content!");
            question.setCommentCount(i + new Random().nextInt(100));
            question.setCreatedDate(new Date(System.currentTimeMillis() + i * 3600));
            question.setUserId(i);
            question.setTitle("title number " + i);
            questionDAO.addQuestion(question);
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

