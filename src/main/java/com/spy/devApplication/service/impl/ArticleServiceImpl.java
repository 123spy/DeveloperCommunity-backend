package com.spy.devApplication.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.spy.devApplication.mapper.ArticleMapper;
import com.spy.devApplication.modal.entity.Article;
import com.spy.devApplication.service.ArticleService;
import org.springframework.stereotype.Service;

@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements ArticleService {
}
