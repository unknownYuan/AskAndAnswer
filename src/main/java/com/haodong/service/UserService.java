package com.haodong.service;

import com.haodong.dao.UserDAO;
import com.haodong.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by h on 17-2-20.
 */
@Service
public class UserService {
    @Autowired
    UserDAO userDAO;

    public User getUser(int id) {
        return userDAO.queryById(id);
    }
}
