package com.haodong.controller;

import com.haodong.model.User;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.*;

/**
 * Created by h on 17-2-17.
 */
@Controller
public class indexController {
    //练习使用路径变量
    @RequestMapping(path = {"/", "/profile/{userId}"})
    @ResponseBody
    public String index(@PathVariable("userId") String userId,
                        HttpSession httpSession) {
        return userId + " msg: " + httpSession.getAttribute("msg");
    }

    //练习使用请求里的变量
    @RequestMapping(path = "/profile")
    @ResponseBody
    public String requestPara(@RequestParam(value = "id", defaultValue = "100", required = false) int id,
                              @RequestParam(value = "userName", defaultValue = "200", required = false) String userName) {
        return "id: " + id + " , username: " + userName;
    }

    //练习使用模板
    @RequestMapping(path = "/temp", method = RequestMethod.GET)
    public String temp(Model m) {
        //利用模板显示数据结构
        m.addAttribute("value1", "this is oneValue");
        List<String> ls = Arrays.asList(new String[]{"green", "red", "blue"});
        m.addAttribute("list", ls);
        Map<String, String> ms = new HashMap<>();
        for (int i = 0; i < 10; i++) {
            ms.put(String.valueOf(i), String.valueOf(i * i));
        }
        m.addAttribute("ms", ms);
        //ms.keySet();
        //添加自定义对象
        m.addAttribute("user", new User("haodong"));
        return "home";
    }

    //练习使用
    @RequestMapping(path = "/request", method = RequestMethod.GET)
    @ResponseBody
    public String request(Model model,
                          HttpServletRequest httpServletRequest,
                          HttpServletResponse httpServletResponse,
                          HttpSession httpSession,
                          @CookieValue("JSESSIONID") String sessionId) {
        StringBuilder sb = new StringBuilder();
//        sb.append("1" + httpServletRequest.getContextPath() + "<br>");
//        sb.append("2" + httpServletRequest.getCookies() + "<br>");
//        sb.append("3" + httpServletRequest.getMethod() + "<br>");
//        sb.append("4" + httpServletRequest.changeSessionId() + "<br>");

        Enumeration<String> headerNames = httpServletRequest.getHeaderNames();
        int i = 0;
        while (headerNames.hasMoreElements()) {
            sb.append(String.valueOf(i++) + "  " + ": " + httpServletRequest.getHeader(headerNames.nextElement()) + "<br>");
        }
        sb.append("JsessionId: " + sessionId);

        //在response头部添加一些信息
        httpServletResponse.addHeader("addHeader", "this is a test");
        //添加一个cookie
        httpServletResponse.addCookie(new Cookie("cookie name", "cookie value"));
        return sb.toString();
    }

    @RequestMapping(path = "/red/{code}", method = RequestMethod.GET)
    public RedirectView redirect(@PathVariable("code") int code,
                                 HttpSession httpSession) {
        httpSession.setAttribute("msg", "this is session msg!");
        //重定向到主页，３０２
//        return "redirect:/";

        RedirectView rv = new RedirectView("/profile", true);
        if (code == 301) {
            rv.setStatusCode(HttpStatus.MOVED_PERMANENTLY);
        }
        return rv;
    }


    @RequestMapping(path = {"/admin"}, method = RequestMethod.GET)
    @ResponseBody
    public String admin(@RequestParam("key") String key) {
        if (key.trim().equals("admin")) {
            return "hello, admin";
        } else {
            throw new IllegalArgumentException("illegalArgument!");
        }
    }

    //练习异常处理
    @ExceptionHandler()
    @ResponseBody
    public String error(Exception e) {
        return "error: " + e;
    }
}