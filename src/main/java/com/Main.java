package com;

import com.mvc.model.User;
import com.mvc.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;

/**
 * Created by linchuanli on 2018/7/4.
 */
public class Main {
    private final static Logger logger = LoggerFactory.getLogger(Main.class);
    @Resource(name="UserService")
    private static UserService userService;

    public static void main(String[] args) {
        System.out.println("run main");
        User user = new User("uid","llc", 0.0d);
        userService.addUser(user);
    }
}
