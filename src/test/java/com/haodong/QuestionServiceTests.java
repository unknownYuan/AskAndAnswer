package com.haodong;

import com.haodong.model.Question;
import com.haodong.service.IExcutor;
import com.haodong.service.QuestionService;
import com.haodong.util.HighConcurrencyExcutor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 这个接口测试高并发往mysql中同时插入帖子
 */
@RunWith(SpringJUnit4ClassRunner.class)
//@SpringApplicationConfiguration(classes = WendaApplication.class)
@SpringBootTest
public class QuestionServiceTests {

    @Autowired
    QuestionService questionService;

    @Autowired
    HighConcurrencyExcutor highConcurrencyExcutor;
    static Random random = new Random();


    /**
     * 高并发插入question
     */
    @Test
//    @Rollback(value = false)
    public void insertQuestion() {
        AtomicInteger successCount = new AtomicInteger(0);

        long start = System.currentTimeMillis();
        highConcurrencyExcutor.run(new IExcutor() {
            @Override
            public void excutor() {
                Question question = new Question();
                question.setContent("content");
                question.setTitle("titile");
                question.setUserId(random.nextInt(100));
                question.setCreatedDate(new Date());
                question.setCommentCount(10000);
                int success = questionService.addQuestion(question);
                if (success > 0) {
                    successCount.incrementAndGet();
                    System.out.println(successCount.get());
                }

            }
        }, 1900, 7600);
        long end = System.currentTimeMillis();
        System.out.println("time = " + (end - start));

    }

}
