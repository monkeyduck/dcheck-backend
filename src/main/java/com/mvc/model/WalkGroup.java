package com.mvc.model;

/**
 * Created by linchuanli on 2018/7/16.
 */
public class WalkGroup extends Group {
    private int walkTarget;

    public WalkGroup(String groupName, String groupOwnerWxid, int walkTarget) {
        super(groupName, groupOwnerWxid);
        this.walkTarget = walkTarget;
    }

    public int getWalkTarget() {
        return walkTarget;
    }

    @Override
    public void setWalkTarget(int walkTarget) {
        this.walkTarget = walkTarget;
    }
}
