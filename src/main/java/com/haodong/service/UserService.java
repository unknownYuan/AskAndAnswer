package com.haodong.service;

import com.haodong.dao.LoginTicketDAO;
import com.haodong.dao.UserDAO;
import com.haodong.model.LoginTicket;
import com.haodong.model.User;
import com.haodong.util.WendaUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by h on 17-2-20.
 */
@Service
public class UserService {
    @Autowired
    UserDAO userDAO;
    @Autowired
    LoginTicketDAO loginTicketDAO;

    public Map<String, String> register(String userName, String password) {
        Map<String, String> map = new HashMap<>();
        if (StringUtils.isBlank(userName)) {
            map.put("msg", "用户名不能为空");
            return map;
        }
        if (StringUtils.isBlank(password)) {
            map.put("msg", "密码不能为空");
        }
        User user = userDAO.queryByName(userName);
        if (user != null) {
            map.put("msg", "用户已经存在");
            return map;
        }
        user = new User();
        user.setName(userName);
        user.setHeadUrl(String.format("http://images.nowcoder.com/head/%dt.png", new Random().nextInt(1000)));
        user.setSalt(UUID.randomUUID().toString().substring(0, 5));
        user.setPassword(WendaUtil.MD5(password.concat(user.getSalt())));
        userDAO.addUser(user);

        //注册完成之后登陆 bug:user.id = 0,newUser.id = auto_increment value
        User newUser = userDAO.queryByName(user.getName());
        String ticket = addLoginTicket(newUser.getId());
        map.put("ticket", ticket);
        return map;//如果注册正常，map中存在ticket
    }

    public Map<String, String> login(String userName, String password) {
        Map<String, String> map = new HashMap<>();
        if (StringUtils.isBlank(userName)) {
            map.put("msg", "用户名不能为空");
            return map;
        }
        if (StringUtils.isBlank(password)) {
            map.put("msg", "密码不能为空");
        }
        User user = userDAO.queryByName(userName);
        if (user == null) {
            map.put("msg", "用户不存在");
            return map;
        }
        if (!user.getPassword().equals(WendaUtil.MD5(password.concat(user.getSalt())))) {
            map.put("msg", "密码错误");
            return map;
        }
        User newUser  = userDAO.queryByName(userName);
        String ticket = addLoginTicket(newUser.getId());
        map.put("ticket", ticket);
        return map;//如果登陆正常，map中存在一个token
    }

    public String addLoginTicket(int userId) {
        LoginTicket loginTicket = new LoginTicket();
        loginTicket.setUserId(userId);
        Date date = new Date();
        //set Date
        date.setTime(3600 * 24 * 100 + date.getTime());
        loginTicket.setExpired(date);
        // 0 代表有效，１表示无效
        loginTicket.setStatus(0);
        loginTicket.setTicket(UUID.randomUUID().toString().replace("-", ""));
        loginTicketDAO.addTicket(loginTicket);
        return loginTicket.getTicket();
    }

    public User getUser(int id) {
        return userDAO.queryById(id);
    }

    public void logout(String ticket) {
        loginTicketDAO.updateStatus(ticket, 1);
    }
}
