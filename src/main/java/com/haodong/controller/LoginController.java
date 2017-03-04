package com.haodong.controller;

import com.haodong.async.EventModel;
import com.haodong.async.EventProducer;
import com.haodong.async.EventType;
import com.haodong.service.UserService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;


@Controller
public class LoginController {
    private static Logger logger = LoggerFactory.getLogger(LoginController.class);
    @Autowired
    UserService userService;

    @Autowired
    EventProducer eventProducer;

    //登陆模块
    @RequestMapping(path = {"/login/"})
    public String login(Model model,
                        @RequestParam(value = "username") String username,
                        @RequestParam(value = "password") String password,
                        @RequestParam(value = "next", required = false) String next,
                        @RequestParam(value = "rememberme", defaultValue = "false", required = false) boolean rememberme,
                        HttpServletResponse httpServletResponse) {
        Map<String, String> maps = userService.login(username, password);
        try {
            if (!maps.containsKey("ticket")) {//如果不存在token，说明登陆失败
                model.addAttribute("msg", maps.get("msg"));//获取失败信息
                return "login";//返回到登陆页面
            } else {//如果登陆成功,就显示主页,主页显示的信息也应该是用户最新的10条信息
                Cookie cookie = new Cookie("ticket", maps.get("ticket"));
                cookie.setPath("/");
                httpServletResponse.addCookie(cookie);
                //判断收件人登陆的时候是否ip异常,这是邮件的收件人邮箱！
                eventProducer.fireEvent(new EventModel(EventType.LOGIN)
                        .setExt("email", "1715976003@qq.com")
                        .setExt("username", username)
                        .setActorId(userService.getUserByName(username).getId()));
                if (StringUtils.isBlank(next)) {
                    return "redirect:/";
                } else {
                    String res = "redirect:" + next;
                    return res;
                }
            }
        } catch (Exception e) {
            logger.error("登陆异常" + e.getMessage());
            return "login";
        }
    }

    @RequestMapping(path = {"/reg/"})
    public String reg(Model model,
                      @RequestParam("username") String username,
                      @RequestParam("password") String password,
                      HttpServletResponse httpServletResponse) {
        Map<String, String> map = userService.register(username, password);
        if (map.containsKey("ticket")) {
            Cookie cookie = new Cookie("ticket", map.get("ticket"));
            cookie.setPath("/");
            httpServletResponse.addCookie(cookie);
            return "redirect:/";//重定向到主页
        } else {
            model.addAttribute("msg", map.get("msg"));
            return "login";//login表示login.html
        }
    }

    @RequestMapping(path = "/reglogin", method = RequestMethod.GET)
    public String reglogin(@RequestParam(value = "next", required = false) String next, Model model) {
        model.addAttribute("next", next);
        return "login";
    }

    @RequestMapping(path = "/logout", method = RequestMethod.GET)
    public String logout(@CookieValue("ticket") String ticket) {
        userService.logout(ticket);
        return "redirect:/";
    }
}
