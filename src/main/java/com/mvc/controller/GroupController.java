package com.mvc.controller;

import com.elasticsearch.ESClient;
import com.elasticsearch.ESClientStore;
import com.mvc.model.Group;
import com.mvc.service.GroupService;
import org.apache.ibatis.annotations.Param;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static final Logger logger = LoggerFactory.getLogger(GroupController.class);

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
        try {
            groupService.createGroup(group);
        } catch (Exception e) {
            logger.error("Mysql error: ", e);
            return e.getMessage();
        }

        ESClientStore.getInstance().getEsClient().createGroup(group);
        return "Create group successfully";
    }

    @RequestMapping(method = RequestMethod.POST, value = "/join")
    @ResponseBody
    public String joinInGroup(HttpServletRequest request) {
        String groupId = request.getParameter("groupId");
        String wxId = request.getParameter("wxId");
        try {
            ESClientStore.getInstance().getEsClient().joinInGroup(groupId, wxId);
        } catch (Exception e) {
            logger.error("error occurs in check function, {}", e);
            return "Failed to check: " + e.getMessage();
        }
        return "User " + wxId + " join in group " + groupId + " successfully!";
    }

    @RequestMapping(method = RequestMethod.POST, value = "/check")
    @ResponseBody
    public String check(HttpServletRequest request) {
        String groupId = request.getParameter("groupId");
        String wxId = request.getParameter("wxId");
        String checkTime = request.getParameter("checkTime");
        String sValue = request.getParameter("value").trim();
        int value;
        String date = new DateTime().toString("yyyy-MM-dd");
        try {
            if (!sValue.equalsIgnoreCase("")) {
                value = Integer.parseInt(sValue);
            } else {
                value = -1;
            }
            ESClientStore.getInstance().getEsClient().check(groupId, wxId, date, checkTime, value);
        } catch (Exception e) {
            logger.error("error occurs in check function, {}", e);
            return "Failed to check: " + e.getMessage();
        }
        return "User " + wxId + " check in group " + groupId + "on " + date + " successfully!";
    }

}
