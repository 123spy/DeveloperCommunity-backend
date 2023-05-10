package com.spy.devApplication.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.spy.devApplication.modal.entity.User;

import javax.servlet.http.HttpServletRequest;

/**
 * 用户服务
 */
public interface UserService extends IService<User> {

    Long userRegister(String userPhone, String userPassword);

    User userLogin(String userLoginPhone, String userLoginPassword, HttpServletRequest request);

    User getLoginUser(HttpServletRequest request);
}
