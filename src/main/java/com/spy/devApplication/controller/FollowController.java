package com.spy.devApplication.controller;

import com.spy.devApplication.common.BaseResponse;
import com.spy.devApplication.service.FollowService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/follow")
@Slf4j
public class FollowController {
    @Resource
    private FollowService followService;

    // 关注
    public BaseResponse followUser() {
        return null;
    }

    // 取消关注

    // 获取全部关注用户的信息

}
