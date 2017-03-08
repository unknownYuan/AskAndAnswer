package com.haodong.configuration;

import com.haodong.interceptor.LoginRequiredInterceptor;
import com.haodong.interceptor.PassportInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;


@Component
public class WendaWebConfiguration extends WebMvcConfigurerAdapter {
    @Autowired
    PassportInterceptor passportInterceptor;
    @Autowired
    LoginRequiredInterceptor loginRequiredInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //注册拦截器
        registry.addInterceptor(passportInterceptor);
        //拦截器的顺序问题，第一个拦截器对第二个有影响,从这里开始继续写
        registry.addInterceptor(loginRequiredInterceptor)
                .addPathPatterns("/pullFeeds")
                .addPathPatterns("/pushFeeds")
                .addPathPatterns("/followUser")
                .addPathPatterns("/followQuestion")
                .addPathPatterns("/question/add")
                .addPathPatterns("/")
                .addPathPatterns("/like")
                .addPathPatterns("/addComment")
                .addPathPatterns("/dislike")
                .addPathPatterns("/msg/notification");
        super.addInterceptors(registry);
    }
}
