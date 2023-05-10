package com.spy.devApplication.modal.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 用户类
 */

@Data
@TableName(value = "user")
public class User {
    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 用户昵称
     */
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private String userName;

    /**
     * 用户手机
     */
    private String userPhone;

    /**
     * 用户头像
     */
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private String userAvatar;

    /**
     * 用户身份
     */
    private String userRole;

    /**
     * 用户密码
     */
    private String userPassword;

    /**
     * 创建时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 更新时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    /**
     * 是否删除
     */
    @TableLogic
    private Integer isDelete;
}
