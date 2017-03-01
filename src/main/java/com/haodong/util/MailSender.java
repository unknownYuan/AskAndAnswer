package com.haodong.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;

/**
 * Created by torch on 17-3-1.
 */
@Service
public class MailSender implements InitializingBean{
    private static final Logger logger = LoggerFactory.getLogger(MailSender.class);
    private JavaMailSenderImpl mailSender = null;

    @Override
    public void afterPropertiesSet() throws Exception {
        mailSender = new JavaMailSenderImpl();
//        mailSender.setUsername("1593028064@qq.com");
//        mailSender.setPassword("liming1234");
    }
}
