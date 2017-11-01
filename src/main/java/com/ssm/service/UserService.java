package com.ssm.service;

import com.ssm.model.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by wangbin on 2017/9/23 0023.
 */
public interface UserService {
    List<User> getAllUser();

    User getUserByPhoneOrEmail(@Param("emailOrPhone") String emailOrPhone, @Param("state") Short state);

    User getUserById(Long userId);

    int batchDeleteUser();
}
