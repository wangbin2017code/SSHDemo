package com.ssm.service.impl;

import com.ssm.dao.UserDao;
import com.ssm.model.User;
import com.ssm.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangbin on 2017/9/23 0023.
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class UserServiceImpl implements UserService {

    @Resource
    private UserDao userDao;

    public User getUserById(Long userId) {
        return userDao.selectUserById(userId);
    }

    public int batchDeleteUser() {
        List<User> userList = new ArrayList<User>();
        User user = new User("test1", "12345678901", "1234444@qq.com");
        User user1 = new User("test2", "123112211", "12323@162.com");
        User user2 = new User("test3", "44341323", "31121111143@qq.com");
        userList.add(user);
        userList.add(user1);
        userList.add(user2);
        return userDao.batchDeleteUser(userList);
    }

    public User getUserByPhoneOrEmail(String emailOrPhone, Short state) {
        return userDao.selectUserByPhoneOrEmail(emailOrPhone, state);
    }

    public List<User> getAllUser() {
        return userDao.selectAllUser();
    }
}
