package com.mvc.controller;

import com.mvc.model.User;
import com.mvc.service.UserService;
import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * Created by linchuanli on 2018/7/4.
 */
@Controller
@RequestMapping("/user")
public class UserController {
    private final static Logger logger = LoggerFactory.getLogger(UserController.class);

    @Resource
    private UserService userService;

    @RequestMapping("add")
    @ResponseBody
    public String addUser(@Param("uid") String uid, @Param("name") String name) {
        logger.info("Into addUser function...");
        User user = new User(uid, name, 0.0d);
        userService.addUser(user);
        return "Inserted successfully.";
    }

    @RequestMapping("test")
    @ResponseBody
    public String queryUser() {
        return userService.queryUser();
    }
}
