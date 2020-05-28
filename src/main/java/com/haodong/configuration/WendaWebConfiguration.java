package com.haodong.configuration;

import com.haodong.interceptor.LoginRequiredInterceptor;
import com.haodong.interceptor.PassportInterceptor;
import com.haodong.interceptor.PrintRequestInfoInterceptor;
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

    @Autowired
    PrintRequestInfoInterceptor printRequestInfoInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(passportInterceptor);
        registry.addInterceptor(loginRequiredInterceptor).addPathPatterns("/user/*");
        registry.addInterceptor(printRequestInfoInterceptor);
        super.addInterceptors(registry);
    }
}
