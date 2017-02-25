package com.haodong.service;

import com.haodong.dao.CommentDAO;
import com.haodong.model.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentService {
    @Autowired
    CommentDAO commentDAO;

    public List<Comment> getCommentByEntity(int entityId, int entityType) {
        return commentDAO.selectCommentByEntity(entityId, entityType);
    }

    /**
     * 添加一条评论
     * @param comment
     * @return
     */
    public int addComment(Comment comment) {
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
}
