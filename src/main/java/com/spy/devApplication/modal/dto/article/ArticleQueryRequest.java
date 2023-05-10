package com.spy.devApplication.modal.dto.article;

import com.spy.devApplication.modal.dto.common.PageRequest;
import lombok.Data;

import java.util.Date;

@Data
public class ArticleQueryRequest extends PageRequest {
    /**
     * id
     */
    private Long id;

    /**
     * 作者id
     */
    private Long authorId;

    /**
     * 文章标题
     */
    private String title;

    /**
     * 文章内容
     */
    private String content;

    /**
     * 创建时间-开始
     */
    private Date createTimeStart;

    /**
     * 创建时间-结束
     */
    private Date createTimeEnd;

    /**
     * 更新时间-开始
     */
    private Date updateTimeStart;

    /**
     * 更新时间-结束
     */
    private Date updateTimeEnd;
}
