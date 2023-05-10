package com.spy.devApplication.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.spy.devApplication.modal.entity.Article;
import org.springframework.stereotype.Repository;


@Repository
public interface ArticleMapper extends BaseMapper<Article> {
}
