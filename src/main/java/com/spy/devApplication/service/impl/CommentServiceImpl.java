package com.spy.devApplication.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.spy.devApplication.mapper.CommentMapper;
import com.spy.devApplication.modal.entity.Comment;
import com.spy.devApplication.service.CommentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {
}
