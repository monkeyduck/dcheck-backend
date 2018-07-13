package com.mvc.service;

import com.mvc.dao.UserDao;
import com.mvc.model.User;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by linchuanli on 2018/7/4.
 */
@Service("UserService")
public class UserServiceImpl implements UserService {
    @Resource
    private UserDao userDao;

    @Override
    public void addUser(User u) {
        userDao.addUser(u);
    }

    @Override
    public String queryUser() {
        return userDao.queryUser();
    }
}
