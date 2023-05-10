package com.spy.devApplication.modal.dto.user;

import lombok.Data;

/**
 * 用户注册请求类
 */
@Data
public class UserRegisterRequest {

    /**
     * 用户密码
     */
    private String userPassword;

    /**
     * 用户电话
     */
    private String userPhone;

}
