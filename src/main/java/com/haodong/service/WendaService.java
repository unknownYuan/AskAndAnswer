package com.haodong.service;

import org.springframework.stereotype.Service;

/**
 * Created by haodong on 10.
 */
@Service
public class WendaService {
    public String getMessage(int userId) {
        return "Hello Message:" + String.valueOf(userId);
    }
}
