package com.haodong;

import com.haodong.controller.LoginController;
import com.haodong.service.LikeService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
//@SpringApplicationConfiguration(classes = WendaApplication.class)
@SpringBootTest
@WebAppConfiguration
public class WendaApplicationTests {

	@Test
	public void contextLoads() {
		System.out.println("test");
	}

	@Autowired
	LoginController loginController;

	@Test
	void login(){

	}

	@Autowired
	LikeService likeService;

	@Test
	void like(){
//		likeService.like();
	}

}
