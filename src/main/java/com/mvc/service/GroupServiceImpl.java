package com.mvc.service;

import com.mvc.dao.GroupDao;
import com.mvc.model.Group;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by linchuanli on 2018/7/4.
 */
@Service("GroupService")
public class GroupServiceImpl implements GroupService {
    @Resource
    private GroupDao groupDao;


    @Override
    public void createGroup(Group group) {
        groupDao.createGroup(group);
    }
}
