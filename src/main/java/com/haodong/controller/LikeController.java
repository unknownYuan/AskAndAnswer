package com.haodong.controller;

import com.haodong.model.HostHolder;
import com.haodong.service.LikeService;
import com.haodong.util.EntityType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created by torch on 17-2-28.
 */
@Controller
public class LikeController {
    private static final Logger logger = LoggerFactory.getLogger(LikeController.class);
    @Autowired
    LikeService likeService;

    @Autowired
    HostHolder hostHolder;

    /**
     * 对评论点赞同
     *
     * @param commentId
     * @return
     */
    @RequestMapping(path = "/like")
    public String like(@RequestParam("commentId") int commentId) {
        if (hostHolder.getUser() == null) {
            return "redirect:/reglogin";
        }
        long likeCount = likeService.like(hostHolder.getUser().getId(), EntityType.COMMENT_TYPE, commentId);
        return "redirect:/";
    }

    @RequestMapping(path = "/dislike")
    public String disLike(@RequestParam("commentId") int commentId){
        if(hostHolder.getUser() == null){
            return "redirect:/reglogin";
        }
        long disLikeCount = likeService.disLike(hostHolder.getUser().getId(), EntityType.COMMENT_TYPE, commentId);
        return "redirect:/";
    }
}
