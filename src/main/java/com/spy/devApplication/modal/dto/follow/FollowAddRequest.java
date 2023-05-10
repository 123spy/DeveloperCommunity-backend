package com.spy.devApplication.modal.dto.follow;

import lombok.Data;

@Data
public class FollowAddRequest {

    // 被关注
    private Long userId;

    // 关注用户Id
    private Long followUserId;
}
