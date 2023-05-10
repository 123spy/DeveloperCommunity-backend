package com.spy.devApplication.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.spy.devApplication.modal.entity.Comment;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentMapper extends BaseMapper<Comment> {
}
