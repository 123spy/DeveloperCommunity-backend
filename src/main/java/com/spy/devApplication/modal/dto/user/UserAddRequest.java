package com.spy.devApplication.modal.dto.user;

import lombok.Data;

import java.util.Date;

/**
 * 用户添加请求类
 */
@Data
public class UserAddRequest {

    /**
     * 用户昵称
     */
    private String userName;

    /**
     * 用户手机
     */
    private String userPhone;

    /**
     * 用户密码
     */
    private String userPassword;

    /**
     * 用户头像
     */
    private String userAvatar;

    /**
     * 用户身份
     */
    private String userRole;
}
