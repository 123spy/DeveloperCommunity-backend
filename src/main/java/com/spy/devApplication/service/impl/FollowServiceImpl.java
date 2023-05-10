package com.spy.devApplication.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.spy.devApplication.mapper.FollowMapper;
import com.spy.devApplication.modal.entity.Follow;
import com.spy.devApplication.service.FollowService;
import org.springframework.stereotype.Service;

@Service
public class FollowServiceImpl extends ServiceImpl<FollowMapper, Follow> implements FollowService {
}
