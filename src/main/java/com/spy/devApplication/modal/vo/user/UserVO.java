package com.spy.devApplication.modal.vo.user;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 用户视图
 */

@Data
public class UserVO {
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
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;
}
