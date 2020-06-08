package com.haodong;

import com.haodong.service.IThreadExcutor;
import com.haodong.service.LikeService;
import com.haodong.util.CountDownLatchExcutor;
import org.junit.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.concurrent.atomic.AtomicInteger;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = WendaApplication.class)
public class LikeServiceTests {
    @Autowired
    LikeService likeService;

    @Autowired
    CountDownLatchExcutor countDownLatchExcutor;

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
        System.out.println(likeService.getLikeCount(1,1));


        AtomicInteger userId = new AtomicInteger(70000);

        countDownLatchExcutor.run(new IThreadExcutor() {
            @Override
            public void excutor() {
                likeService.like(userId.getAndIncrement(), 1, 1);
            }
        }, 200 ,5000);

        System.out.println(likeService.getLikeCount(1,1));
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
