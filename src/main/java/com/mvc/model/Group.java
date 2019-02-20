package com.mvc.model;

import com.utils.Utils;
import org.joda.time.DateTime;
import org.springframework.jdbc.support.nativejdbc.OracleJdbc4NativeJdbcExtractor;

import java.util.*;


/**
 * Created by linchuanli on 2018/7/11.
 */
public class Group {
    public static final int GROUP_ID_MAX_LENGTH = 16;
    private String groupId;
    private String groupName;
    private String groupOwnerWxid;
    private String groupType;
    private String createTime;
    private String startTime;
    private int durationDays;
    private int amount;
    private String checkTimeFrom;
    private String checkTimeTo;
    private int walkTarget;
    List<String> members;
    Map<String, Map<String, CheckRecord>> checkDetail;

    public Group() {}

    public Group(String groupName, String groupOwnerWxid, String startTime) {
        this.groupName = groupName;
        this.groupOwnerWxid = groupOwnerWxid;
        DateTime dt = new DateTime();
        this.createTime = dt.toString("yyyy-MM-dd HH:mm:ss");
        this.startTime = startTime;
        this.groupId = genGroupId(groupName, groupOwnerWxid, startTime);
        members = new ArrayList<>();
        members.add(groupOwnerWxid);
        checkDetail = new HashMap<>();
        initCheckDetails(this.groupOwnerWxid, this.startTime, this.durationDays);
    }

    public Group(String groupName, String groupOwnerWxid, String groupType, String startTime, int durationDays,
                 int amount, String checkTimeFrom, String checkTimeTo) {
        this.groupName = groupName;
        this.groupOwnerWxid = groupOwnerWxid;
        this.groupType = groupType;
        DateTime dt = new DateTime();
        this.createTime = dt.toString("yyyy-MM-dd HH:mm:ss");
        this.startTime = startTime;
        this.durationDays = durationDays;
        this.amount = amount;
        this.groupId = genGroupId(groupName, groupOwnerWxid, startTime);
        this.checkTimeFrom = checkTimeFrom;
        this.checkTimeTo = checkTimeTo;
        members = new ArrayList<>();
        members.add(groupOwnerWxid);
        checkDetail = new HashMap<>();
        initCheckDetails(this.groupOwnerWxid, this.startTime, this.durationDays);
    }


    public void initCheckDetails(String wxId, String startDate, int duration) {
        DateTime dtStart = new DateTime(startDate);
        for (int i = 0; i < duration; ++i) {
            DateTime dt = dtStart.plusDays(i);
            String sDate = dt.toString("yyyy-MM-dd");
            Map<String, CheckRecord> recordMap = new HashMap<>();
            recordMap.put(wxId, new CheckRecord());
            this.checkDetail.put(sDate, recordMap);
        }
    }

    private String genGroupId(String groupName, String groupOwnerWxid, String startTime) {
        String data = groupName + groupOwnerWxid + startTime;
        String originalResult = Utils.encodeMD5(data);
        if (originalResult.length() <= GROUP_ID_MAX_LENGTH) {
            return originalResult;
        }
        else {
            return originalResult.substring(0, GROUP_ID_MAX_LENGTH);
        }
    }

    public void addMember(String wxId) throws Exception {
        if (this.members.contains(wxId)) throw new Exception("add duplicate members!");
        Iterator<Map.Entry<String, Map<String, CheckRecord>>> iterator = this.checkDetail.entrySet().iterator();

        while(iterator.hasNext()) {
            Map<String, CheckRecord> map = iterator.next().getValue();
            map.put(wxId, new CheckRecord());
        }
        this.members.add(wxId);
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
        return groupType;
    }

    public void setGroupType(String groupType) {
        this.groupType = groupType;
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

    public String getCheckTimeFrom() {
        return checkTimeFrom;
    }

    public void setCheckTimeFrom(String checkTimeFrom) {
        this.checkTimeFrom = checkTimeFrom;
    }

    public String getCheckTimeTo() {
        return checkTimeTo;
    }

    public void setCheckTimeTo(String checkTimeTo) {
        this.checkTimeTo = checkTimeTo;
    }

    public int getWalkTarget() {
        return walkTarget;
    }

    public void setWalkTarget(int walkTarget) {
        this.walkTarget = walkTarget;
    }

    public List<String> getMembers() {
        return members;
    }

    public void setMembers(List<String> members) {
        this.members = members;
    }

    public Map<String, Map<String, CheckRecord>> getCheckDetail() {
        return checkDetail;
    }

    public void setCheckDetail(Map<String, Map<String, CheckRecord>> checkDetail) {
        this.checkDetail = checkDetail;
    }
}
