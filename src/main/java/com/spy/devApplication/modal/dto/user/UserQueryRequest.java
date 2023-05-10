package com.spy.devApplication.modal.dto.user;

import com.spy.devApplication.modal.dto.common.PageRequest;
import lombok.Data;

import java.util.Date;

@Data
public class UserQueryRequest extends PageRequest {
    /**
     * id
     */
    private Long id;

    /**
     * 用户昵称
     */
    private String userName;

    /**
     * 用户手机
     */
    private String userPhone;

    /**
     * 用户头像
     */
    private String userAvatar;

    /**
     * 用户身份
     */
    private String userRole;


    /**
     * 创建时间-开始
     */
    private Date createTimeStart;

    /**
     * 创建时间-结束
     */
    private Date createTimeEnd;

    /**
     * 更新时间-开始
     */
    private Date updateTimeStart;

    /**
     * 更新时间-结束
     */
    private Date updateTimeEnd;
}
