package com.haodong.controller;

import com.haodong.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * Created by h on 17-2-21.
 */
@Controller
public class LoginController {
    private static Logger logger = LoggerFactory.getLogger(LoginController.class);
    @Autowired
    UserService userService;

    //登陆模块

    @RequestMapping(path = {"/login/"})
    public String login(Model model,
                        @RequestParam("username") String username,
                        @RequestParam("password") String password,
                        @RequestParam(value = "rememberme", defaultValue = "false") boolean rememberme,
                        HttpServletResponse httpServletResponse) {
        Map<String, String> maps = userService.login(username, password);
        try {
            if (!maps.containsKey("ticket")) {
                model.addAttribute("msg", maps.get("msg"));
                return "login";
            } else {
                Cookie cookie = new Cookie("ticket", maps.get("ticket"));
                cookie.setPath("/");
                httpServletResponse.addCookie(cookie);
                return "redirect:/";
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
    public String reglogin() {
        return "login";
    }

    @RequestMapping(path = "/logout", method = RequestMethod.GET)
    public String logout(@CookieValue("ticket") String ticket) {
        userService.logout(ticket);
        return "redirect:/";
    }
}
