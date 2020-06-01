package com.haodong.service;

import com.haodong.dao.LogDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LogService {


    @Autowired
    LogDAO logDAO;

    public int record(int id) {
        return logDAO.insert(id);
    }
}
