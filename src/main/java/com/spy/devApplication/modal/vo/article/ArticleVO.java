package com.spy.devApplication.modal.vo.article;

import com.baomidou.mybatisplus.annotation.*;
import com.spy.devApplication.modal.vo.comment.CommentVO;
import com.spy.devApplication.modal.vo.user.UserVO;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

@Data
public class ArticleVO {
    /**
     * id
     */
    private Long id;

    /**
     * 用户对象
     */
    private UserVO author;

    /**
     * 评论对象
     */
    private List<CommentVO> comments;

    /**
     * 用户昵称
     */
    private String title;

    /**
     * 文章内容
     */
    private String content;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;
}
