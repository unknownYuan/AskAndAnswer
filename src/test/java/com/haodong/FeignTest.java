//package com.haodong;
//
//
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.autoconfigure.http.HttpMessageConvertersAutoConfiguration;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.cloud.openfeign.EnableFeignClients;
//import org.springframework.cloud.openfeign.FeignAutoConfiguration;
//import org.springframework.cloud.openfeign.FeignClient;
//import org.springframework.context.annotation.Import;
//import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
//
//@RunWith(SpringJUnit4ClassRunner.class)
//@SpringBootTest(classes = FeignTest.class)
//@Import({FeignAutoConfiguration.class, HttpMessageConvertersAutoConfiguration.class})
//@EnableFeignClients(clients = )
//public class FeignTest {
//
//    @FeignClient(value = "questionFeigin", url = "127.0.0.1:8080")
//    public interface QuestionFeign{
//
//    }
//
//    @Autowired
//    QuestionFeign questionFeign;
//
//
//    @Test
//    public void addQuestion(){
////        questionFeign
//    }
//}
