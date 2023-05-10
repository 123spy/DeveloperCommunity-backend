package com.spy.devApplication.modal.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
@TableName(value = "comment")
public class Comment {

    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    private Long id;


    /**
     * 文章id
     */
    private Long articleId;

    /**
     * 作者id
     */
    private Long authorId;

    /**
     * 评论内容
     */
    private String content;

    /**
     * 创建时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 更新时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    /**
     * 是否删除
     */
    @TableLogic
    private Integer isDelete;
}
