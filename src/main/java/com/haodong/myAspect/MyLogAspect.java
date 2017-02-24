package com.haodong.myAspect;

import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/**
 * Created by h on 17-2-17.
 */
@Aspect
@Component
public class MyLogAspect {

    //Logger必须是同一个包里面的，不能使用强制类型转换
    private Logger l = LoggerFactory.getLogger(MyLogAspect.class);

    //包名.类名.方法名
    @Before("execution(* com.haodong.controller.PracticeIndexController.*(..))")
    public void beforeMethod() {
        l.info("before...");
    }
}
