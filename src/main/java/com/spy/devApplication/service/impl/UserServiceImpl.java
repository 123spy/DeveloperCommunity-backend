package com.spy.devApplication.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.spy.devApplication.common.ErrorCode;
import com.spy.devApplication.exception.BusinessException;
import com.spy.devApplication.mapper.UserMapper;
import com.spy.devApplication.modal.entity.User;
import com.spy.devApplication.service.UserService;
import com.spy.devApplication.utils.BasicUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import static com.spy.devApplication.constant.UserConstant.*;

/**
 * 用户服务实现类
 */

@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Resource
    private UserMapper userMapper;

    @Override
    public Long userRegister(String userPhone, String userPassword) {
        synchronized (userPhone.intern()) {
            QueryWrapper<User> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("userPhone", userPhone);
            long count = this.count(queryWrapper);
            if(count != 0) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "手机号已被注册");
            }

            String md5Password = BasicUtil.md5Password(userPassword);

            User user = new User();
            String userName = BasicUtil.randomName();
            user.setUserName(userName);
            user.setUserPassword(md5Password);
            user.setUserPhone(userPhone);
            user.setUserRole(DEFAULT_ROLE);

            boolean result = this.save(user);
            if(!result) {
                throw new BusinessException(ErrorCode.SYSTEM_ERROR, "注册失败");
            }
            return user.getId();
        }

    }

    @Override
    public User userLogin(String userLoginPhone, String userLoginPassword, HttpServletRequest request) {
        if(!BasicUtil.checkPassword(userLoginPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "密码错误");
        }

        if(!BasicUtil.checkPhone(userLoginPhone)) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "手机号错误");
        }

        // 校验这个人是否存在
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("userPhone", userLoginPhone);
        userQueryWrapper.eq("userPassword", BasicUtil.md5Password(userLoginPassword));

        User user = this.getOne(userQueryWrapper);
        if(user == null) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "登陆失败");
        }

        request.getSession().setAttribute(USER_LOGIN_STATE, user);
        return user;
    }

    @Override
    public User getLoginUser(HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute(USER_LOGIN_STATE);
        if(user == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "未登录");
        }
        Long id = user.getId();
        if(id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = this.getById(id);
        if(loginUser == null || loginUser.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        return loginUser;
    }

}
