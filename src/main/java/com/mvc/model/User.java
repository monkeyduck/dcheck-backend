package com.mvc.model;

import org.joda.time.DateTime;

import java.util.UUID;

/**
 * Created by linchuanli on 2018/7/3.
 */
public class User {
    private int id;
    private String uid;
    private String wxId;
    private String name;
    private String created_time;
    private Double balance;

    public User(String wxId, String name, Double balance) {
        DateTime dt = new DateTime();
        String today = dt.toString("yyyy-MM-dd HH:mm:ss");
        this.wxId = wxId;
        this.name = name;
        this.created_time = today;
        this.balance = balance;
        this.uid = genUUid();
    }

    private String genUUid() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getWxId() {
        return wxId;
    }

    public void setWxId(String wxId) {
        this.wxId = wxId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCreated_time() {
        return created_time;
    }

    public void setCreated_time(String created_time) {
        this.created_time = created_time;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

}
