package com.mvc.model;

import net.sf.json.JSONObject;

/**
 * Created by linchuanli on 2018/7/23.
 */
public class CheckRecord {
    private CheckStatus checkStatus;
    private String checkTime;
    private int value;

    public CheckRecord() {
        this.checkStatus = CheckStatus.Unchecked;
        this.checkTime = "";
        this.value = -1;
    }

    public CheckRecord(CheckStatus checkStatus, String checkTime, int value) {
        this.checkStatus = checkStatus;
        this.checkTime = checkTime;
        this.value = value;
    }

    public CheckStatus getCheckStatus() {
        return checkStatus;
    }

    public void setCheckStatus(CheckStatus checkStatus) {
        this.checkStatus = checkStatus;
    }

    public String getCheckTime() {
        return checkTime;
    }

    public void setCheckTime(String checkTime) {
        this.checkTime = checkTime;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    @Override
    public String toString() {
        JSONObject json = JSONObject.fromObject(this);
        System.out.println(json.toString());
        return json.toString();
    }
}
