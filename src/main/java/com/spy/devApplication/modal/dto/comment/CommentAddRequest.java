package com.spy.devApplication.modal.dto.comment;

import lombok.Data;

@Data
public class CommentAddRequest {
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
}
