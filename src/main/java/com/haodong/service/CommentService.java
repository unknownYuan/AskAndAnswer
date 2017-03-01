package com.haodong.service;

import com.haodong.dao.CommentDAO;
import com.haodong.model.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

@Service
public class CommentService {
    @Autowired
    CommentDAO commentDAO;

    @Autowired
    SensitiveService sensitiveService;
    public List<Comment> getCommentByEntity(int entityId, int entityType) {
        return commentDAO.selectCommentByEntity(entityId, entityType);
    }

    /**
     * 添加一条评论
     * @param comment
     * @return
     */
    public int addComment(Comment comment) {
        //评论的敏感词过滤
        //过滤html标签
        comment.setContent(HtmlUtils.htmlEscape(comment.getContent()));
        comment.setContent(sensitiveService.filter(comment.getContent()));
        return commentDAO.addComment(comment) > 0 ? comment.getId() : 0;
    }

    /**
     * 得到评论数
     * @param entityId
     * @param entityType
     * @return
     */
    public int getComment(int entityId, int entityType){
        return commentDAO.getCommentCount(entityId, entityType);
    }

    /**
     * 删除评论
     * @param id
     * @return
     */
    public int deleteComment(int id){
        // status = 1 表示评论无效
        return commentDAO.updateStatus(id, 1);
    }

    public Comment getCommentById(int id){
        return commentDAO.getCommentById(id);
    }
}
