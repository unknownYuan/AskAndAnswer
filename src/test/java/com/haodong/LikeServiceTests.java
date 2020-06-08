package com.haodong;

import com.haodong.service.IExcutor;
import com.haodong.service.LikeService;
import com.haodong.util.HighConcurrencyExcutor;
import org.junit.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.concurrent.atomic.AtomicInteger;


/**
 * 这个class的作用是测试高并发点赞,1000个线程同时点赞，结果是正确的，原因：redis的sadd
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = WendaApplication.class)
public class LikeServiceTests {
    @Autowired
    LikeService likeService;

    @Autowired
    HighConcurrencyExcutor highConcurrencyExcutor;

    @Before
    public void setUp() {
       System.out.println("setUp");
    }

    @After
    public void tearDown() {
        System.out.println("tearDown");
    }

    @BeforeClass
    public static void beforeClass() {
        System.out.println("beforeClass");
    }

    @AfterClass
    public static void afterClass() {
        System.out.println("afterClass");
    }

    @Test
    public void testLike() {
        System.out.println("testLike");
        final int type = 1;
        final int id = 4;

        System.out.println(likeService.getLikeCount(type,id));


        AtomicInteger userId = new AtomicInteger(10000);

        highConcurrencyExcutor.run(new IExcutor() {
            @Override
            public void excutor() {
                likeService.like(userId.getAndIncrement(), type, id);
            }
        }, 1000 ,5000);

        System.out.println(likeService.getLikeCount(type,id));
    }

    @Test
    public void testXXX() {
        System.out.println("testXXX");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testException() {
        System.out.println("testException");
        throw new IllegalArgumentException("异常发生了");
    }
}
