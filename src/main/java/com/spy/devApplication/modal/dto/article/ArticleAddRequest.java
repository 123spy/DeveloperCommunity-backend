package com.spy.devApplication.modal.dto.article;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 用户添加请求类
 */
@Data
public class ArticleAddRequest {

    /**
     * 文章标题
     */
    private String title;

    /**
     * 文章内容
     */
    private String content;

}
