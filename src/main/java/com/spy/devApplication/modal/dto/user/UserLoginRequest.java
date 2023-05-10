package com.spy.devApplication.modal.dto.user;

import lombok.Data;

/**
 * 用户登录请求类
 */

@Data
public class UserLoginRequest {

    /**
     * 用户密码
     */
    private String userPassword;

    /**
     * 用户电话
     */
    private String userPhone;

}
