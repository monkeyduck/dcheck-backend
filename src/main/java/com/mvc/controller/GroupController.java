package com.mvc.controller;

import com.mvc.model.Group;
import com.mvc.service.GroupService;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

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
    @ResponseBody
    public String createGroup(HttpServletRequest request) {
        String groupName = request.getParameter("groupName");
        String ownerWxid = request.getParameter("groupOwnerWxid");
        String groupType = request.getParameter("groupType");
        String startTime = request.getParameter("startTime");
        int durationDays = Integer.parseInt(request.getParameter("durationDays"));
        int amount       = Integer.parseInt(request.getParameter("amount"));
        String checkFrom = request.getParameter("checkTimeFrom");
        String checkTo   = request.getParameter("checkTimeTo");
        Group group = new Group(groupName, ownerWxid, groupType, startTime, durationDays, amount, checkFrom, checkTo);
        groupService.createGroup(group);
        return "Create group successfully";
    }


}
