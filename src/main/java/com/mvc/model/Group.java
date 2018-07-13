package com.mvc.model;

import com.utils.Utils;
import org.joda.time.DateTime;


/**
 * Created by linchuanli on 2018/7/11.
 */
public class Group {
    public static final int GROUP_ID_MAX_LENGTH = 16;
    private String groupId;
    private String groupName;
    private String groupOwnerWxid;
    private GroupType groupType;
    private String createTime;
    private String startTime;
    private int durationDays;
    private int amount;

    public Group(String groupName, String groupOwnerWxid, String groupType, String startTime, int durationDays,
                 int amount) {
        this.groupName = groupName;
        this.groupOwnerWxid = groupOwnerWxid;
        this.groupType = GroupType.valueOf(groupType);
        DateTime dt = new DateTime();
        this.createTime = dt.toString("yyyy-MM-dd HH:mm:ss");
        this.startTime = startTime;
        this.durationDays = durationDays;
        this.amount = amount;
        this.groupId = genGroupId(groupName, groupOwnerWxid, this.createTime);
    }

    private String genGroupId(String groupName, String groupOwnerWxid, String createTime) {
        String data = groupName + groupOwnerWxid + createTime;
        String originalResult = Utils.encodeMD5(data);
        if (originalResult.length() <= GROUP_ID_MAX_LENGTH) {
            return originalResult;
        }
        else {
            return originalResult.substring(0, GROUP_ID_MAX_LENGTH);
        }
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getGroupOwnerWxid() {
        return groupOwnerWxid;
    }

    public void setGroupOwnerWxid(String groupOwnerWxid) {
        this.groupOwnerWxid = groupOwnerWxid;
    }

    public String getGroupType() {
        return groupType.getType();
    }

    public void setGroupType(String groupType) {
        this.groupType = GroupType.valueOf(groupType);
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public int getDurationDays() {
        return durationDays;
    }

    public void setDurationDays(int durationDays) {
        this.durationDays = durationDays;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
