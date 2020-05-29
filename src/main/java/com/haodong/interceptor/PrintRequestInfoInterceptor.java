package com.haodong.interceptor;

import com.alibaba.fastjson.JSON;
import com.haodong.util.JsonSerializer;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.function.BiConsumer;

@Component
public class PrintRequestInfoInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {
        StringBuffer sb=new StringBuffer();
        sb.append("request {");
        sb.append(JsonSerializer.serialize(httpServletRequest.getParameterMap()));
        sb.append("} " + httpServletRequest.getRequestURI() + "\n");
        if(modelAndView != null) {
            sb.append("response " + JSON.toJSONString(modelAndView));
        }
        System.out.println(sb.toString());
    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
