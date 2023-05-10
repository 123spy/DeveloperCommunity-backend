package com.spy.devApplication.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.spy.devApplication.modal.entity.User;
import org.springframework.stereotype.Repository;

/**
 * @Entity com.spy.devApplication.modal.entity.User
 */

@Repository
public interface UserMapper extends BaseMapper<User> {
}
