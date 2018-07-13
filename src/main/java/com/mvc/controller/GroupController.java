package com.mvc.controller;

import com.mvc.model.Group;
import com.mvc.service.GroupService;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;

/**
 * Created by linchuanli on 2018/7/4.
 */
@Controller
@RequestMapping("/group")
public class GroupController {

    @Resource
    private GroupService groupService;

    @RequestMapping("test")
    public void queryTest() {

    }

    @RequestMapping(method = RequestMethod.POST, value = "/create")
    public void createGroup(Group group) {
        System.out.println(group.getGroupId());
        groupService.createGroup(group);
    }


}
