package com.mvc.dao;

import com.mvc.model.User;

/**
 * Created by linchuanli on 2018/7/4.
 */
public interface UserDao {
    void addUser(User u);
    String queryUser();
}
