package com.spy.devApplication.modal.dto.article;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 用户更新请求类
 */
@Data
public class ArticleUpdateRequest {
    /**
     * id
     */
    private Long id;

    /**
     * 用户ID
     */
    private Long authorId;

    /**
     * 用户昵称
     */
    private String title;

    /**
     * 文章内容
     */
    private String content;
}
