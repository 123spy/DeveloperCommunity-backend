package com.spy.devApplication.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.spy.devApplication.common.BaseResponse;
import com.spy.devApplication.common.ErrorCode;
import com.spy.devApplication.common.ResultUtils;
import com.spy.devApplication.constant.CommonConstant;
import com.spy.devApplication.exception.BusinessException;
import com.spy.devApplication.modal.dto.user.*;
import com.spy.devApplication.modal.entity.User;
import com.spy.devApplication.modal.vo.user.UserVO;
import com.spy.devApplication.service.UserService;
import com.spy.devApplication.utils.BasicUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static com.spy.devApplication.constant.UserConstant.*;

/**
 * 用户接口
 */

@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    @Resource
    private UserService userService;

    // region 登录相关

    /**
     * 用户注册
     * @param userRegisterRequest
     * @return
     */
    @PostMapping("/register")
    public BaseResponse<Long> userRegister(@RequestBody UserRegisterRequest userRegisterRequest, HttpServletRequest request) {
        if(userRegisterRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        String userPassword = userRegisterRequest.getUserPassword();
        String userPhone = userRegisterRequest.getUserPhone();

        if(!BasicUtil.checkPhone(userPhone)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "手机号错误");
        }

        if(!BasicUtil.checkPassword(userPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "密码错误");
        }

        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userPhone", userPhone);
        long count = userService.count(queryWrapper);

        if(count != 0) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "手机号已被注册");
        }

        // 获取随机的用户名
        String userName = BasicUtil.randomName();
        User user = new User();
        BeanUtils.copyProperties(userRegisterRequest, user);
        user.setUserPassword(BasicUtil.md5Password(userPassword));

        Long registerResult = userService.userRegister(userPhone, userPassword);

        if(registerResult == 1) {
            // 如果是一号用户,设为管理员;
            UserUpdateRequest userUpdateRequest = new UserUpdateRequest();
            userUpdateRequest.setId(registerResult);
            userUpdateRequest.setUserRole(ADMIN_ROLE);

            updateUser(userUpdateRequest, request);
        }
        return ResultUtils.success(registerResult);
    }

    /**
     * 登录
     * @param userLoginRequest
     * @param request
     * @return
     */
    @PostMapping("/login")
    public BaseResponse userLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request) {
        if(userLoginRequest == null || request == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        String userLoginPassword = userLoginRequest.getUserPassword();
        String userLoginPhone = userLoginRequest.getUserPhone();


        User loginUser = userService.userLogin(userLoginPhone, userLoginPassword, request);
        if(loginUser == null) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR);
        }
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(loginUser, userVO);
        return ResultUtils.success(userVO);
    }


    @PostMapping("/logout")
    public BaseResponse userLogout(HttpServletRequest request) {
        if(request == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        if((User) request.getSession().getAttribute(USER_LOGIN_STATE) == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
        }
        request.getSession().removeAttribute(USER_LOGIN_STATE);
        return ResultUtils.success(true);

    }

    /**
     * 获取当前登录用户
     * @param request
     * @return
     */
    @GetMapping("/get/login")
    public BaseResponse getLoginUser(HttpServletRequest request) {
        if(request == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = userService.getLoginUser(request);
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user, userVO);
        return ResultUtils.success(userVO);
    }

    // endregion

    // region 增删改查

    /**
     * 添加用户
     * @param userAddRequest
     * @param request
     * @return
     */
    @PostMapping("/add")
    public BaseResponse addUser(@RequestBody UserAddRequest userAddRequest, HttpServletRequest request) {
        if(userAddRequest == null || request == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        // 需要管理员身份
        User loginUser = userService.getLoginUser(request);
        if(!loginUser.getUserRole().equals(ADMIN_ROLE)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }

        User addUser = new User();

        String userName = userAddRequest.getUserName();
        if(StringUtils.isNotBlank(userName)) {
            addUser.setUserName(userName);
        } else {
            addUser.setUserName(BasicUtil.randomName());
        }

        String userPhone = userAddRequest.getUserPhone();
        if(StringUtils.isNotBlank(userPhone)) {
            if(BasicUtil.checkPhone(userPhone)) {
                addUser.setUserPhone(userPhone);
            } else {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "手机号错误");
            }
        } else {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "手机号为空");
        }

        String userPassword = userAddRequest.getUserPassword();
        if(StringUtils.isNotBlank(userPassword)) {
            if(BasicUtil.checkPassword(userPassword)) {
                addUser.setUserPassword(BasicUtil.md5Password(userPassword));
            } else {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "密码错误");
            }
        } else {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "密码为空");
        }

        String userAvatar = userAddRequest.getUserAvatar();
        if(StringUtils.isNotBlank(userAvatar)) {
            addUser.setUserAvatar(userAvatar);
        }

        String userRole = userAddRequest.getUserRole();
        if(StringUtils.isNotBlank(userRole)) {
            if(!(userRole.equals(ADMIN_ROLE) || userRole.equals(DEFAULT_ROLE))) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "身份错误");
            } else {
                addUser.setUserRole(userRole);
            }
        } else {
            addUser.setUserRole(DEFAULT_ROLE);
        }

        boolean addResult = userService.save(addUser);

        if(!addResult) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "添加失败");
        }
        return ResultUtils.success(true);
    }

    /**
     * 删除用户
     * @param id
     * @param request
     * @return
     */
    @GetMapping("/delete")
    public BaseResponse deleteUser(Long id, HttpServletRequest request) {

        if(id == null || id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 获取当前登录用户
        User user = userService.getLoginUser(request);
        if(!(user.getUserRole().equals(ADMIN_ROLE) || id == user.getId())) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }

        boolean deleteResult = userService.removeById(id);
        if(!deleteResult) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "删除失败");
        }
        return ResultUtils.success(true);
    }

    /**
     * 修改用户信息
     * @param userUpdateRequest
     * @param request
     * @return
     */
    @PostMapping("/update")
    public BaseResponse updateUser(@RequestBody UserUpdateRequest userUpdateRequest, HttpServletRequest request) {
        if(userUpdateRequest == null || request == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        // 获取当前登录用户
        User currentUser = userService.getLoginUser(request);

        // 获取需要更新用户的id
        Long id = userUpdateRequest.getId();
        if(id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        // 获取将要更新用户的的对象
        User oldUpdateUser = userService.getById(id);
        if(oldUpdateUser == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        // 需要是是本人或者是管理员
        if(!(oldUpdateUser.getId() == currentUser.getId() || currentUser.getUserRole().equals(ADMIN_ROLE))) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }

        // 更新后用户的对象
        User newUpdateUser = new User();
        newUpdateUser.setId(id);

        // 属性设置为用户名可以设置为null
        String userName = userUpdateRequest.getUserName();

        // 判断有无，在判断是否与旧数据相同
        if(StringUtils.isNotBlank(userName) && !oldUpdateUser.getUserName().equals(userName)) {
            if (BasicUtil.checkUserName(userName)) {
                newUpdateUser.setUserName(userName);
            } else {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户名错误");
            }
        } else {
            newUpdateUser.setUserName(oldUpdateUser.getUserName());
        }

        String userPhone = userUpdateRequest.getUserPhone();
        // 电话修改存在
        if(StringUtils.isNotBlank(userPhone) && !oldUpdateUser.getUserPhone().equals(userPhone)) {
            // 校验电话号码是否合法
            if (BasicUtil.checkPhone(userPhone)) {
                // 校验电话是否被注册过
                QueryWrapper<User> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("userPhone", userPhone);
                long count = userService.count(queryWrapper);
                if(count != 0) {
                    throw new BusinessException(ErrorCode.PARAMS_ERROR, "手机号已被注册");
                }
                newUpdateUser.setUserPhone(userPhone);
            } else {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "手机号错误");
            }
        } else {
            newUpdateUser.setUserPhone(oldUpdateUser.getUserPhone());
        }

        String userAvatar = userUpdateRequest.getUserAvatar();
        if(StringUtils.isNotBlank(userAvatar) && !oldUpdateUser.getUserAvatar().equals(userAvatar)) {
            newUpdateUser.setUserAvatar(userAvatar);
        } else {
            newUpdateUser.setUserAvatar(oldUpdateUser.getUserAvatar());
        }

        String userRole = userUpdateRequest.getUserRole();
        if(StringUtils.isNotBlank(userRole) && !oldUpdateUser.getUserRole().equals(userRole)) {
            // 需要管理员身份
            if(!currentUser.getUserRole().equals(ADMIN_ROLE)) {
                throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
            }

            if(!(userRole.equals(DEFAULT_ROLE) || userRole.equals(ADMIN_ROLE))) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR);
            }

            newUpdateUser.setUserRole(userRole);
        } else {
            newUpdateUser.setUserRole(oldUpdateUser.getUserRole());
        }

        String userPassword = userUpdateRequest.getUserPassword();
        if(StringUtils.isNotBlank(userPassword) && !oldUpdateUser.getUserPassword().equals(BasicUtil.md5Password(userPassword))) {
            if(BasicUtil.checkPassword(userPassword)) {
                String md5Password = BasicUtil.md5Password(userPassword);
                newUpdateUser.setUserPassword(md5Password);
            }
        } else {
            newUpdateUser.setUserPassword(oldUpdateUser.getUserPassword());
        }

        boolean updateResult = userService.updateById(newUpdateUser);

        if(!updateResult) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "更新失败");
        }

        return ResultUtils.success(true);
    }

    /**
     * 根据id获取用户
     * @param id
     * @return
     */
    @GetMapping("/get")
    public BaseResponse getUserById(Long id) {
        if(id <= 0 || id == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        User user = userService.getById(id);

        if(user == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号不存在");
        }
        UserVO userVO = new UserVO();

        BeanUtils.copyProperties(user, userVO);
        return ResultUtils.success(userVO);
    }

    /**
     * 按照页数查询用户
     * @param userQueryRequest
     * @param request
     * @return
     */
    @PostMapping("/list/page")
    public BaseResponse getUserByPage(@RequestBody UserQueryRequest userQueryRequest, HttpServletRequest request) {
        if(userQueryRequest == null || request == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        Integer current = 1;
        Integer pageSize = 20;

        if(userQueryRequest.getCurrent() != null && userQueryRequest.getCurrent() > 0) {
            current = userQueryRequest.getCurrent();
        }
        if(userQueryRequest.getPageSize() != null && userQueryRequest.getPageSize() > 0) {
            pageSize = userQueryRequest.getPageSize();
        }

        Long id = userQueryRequest.getId();
        String userName = userQueryRequest.getUserName();
        String userPhone = userQueryRequest.getUserPhone();
        String userAvatar = userQueryRequest.getUserAvatar();
        String userRole = userQueryRequest.getUserRole();

        Date createTimeStart = userQueryRequest.getCreateTimeStart();
        Date createTimeEnd = userQueryRequest.getCreateTimeEnd();

        Date updateTimeStart = userQueryRequest.getUpdateTimeStart();
        Date updateTimeEnd = userQueryRequest.getUpdateTimeEnd();
        String sortField = userQueryRequest.getSortField();
        String sortOrder = userQueryRequest.getSortOrder();

        QueryWrapper<User> queryWrapper = new QueryWrapper<>();

        queryWrapper.eq(id != null && id > 0,"id", id);
        queryWrapper.like(StringUtils.isNotBlank(userName),"userName", userName);
        queryWrapper.like(StringUtils.isNotBlank(userPhone), "userPhone", userPhone);
        queryWrapper.eq(StringUtils.isNotBlank(userRole),"userRole", userRole);

        // 更新时间
        queryWrapper.ge(updateTimeStart != null, "updateTime", updateTimeStart);
        queryWrapper.le( updateTimeEnd != null, "updateTime", updateTimeEnd);

        // 创建时间
        queryWrapper.ge(createTimeStart != null, "createTime", createTimeStart);
        queryWrapper.le( createTimeEnd != null, "createTime", createTimeEnd);

        // 查询的排序
        queryWrapper.orderBy(StringUtils.isNotBlank(sortField), sortOrder.equals(CommonConstant.SORT_ORDER_ASC), sortField);

        Page<User> userPage = userService.page(new Page<>(current, pageSize), queryWrapper);
        Page<UserVO> userVOPage = new PageDTO<>(userPage.getCurrent(), userPage.getSize(), userPage.getTotal());

        List<UserVO> userVOList = userPage.getRecords().stream().map(user -> {
            UserVO userVO = new UserVO();
            BeanUtils.copyProperties(user, userVO);
            return userVO;
        }).collect(Collectors.toList());
        userVOPage.setRecords(userVOList);
        return ResultUtils.success(userVOPage);
    }
    // endregion
}
