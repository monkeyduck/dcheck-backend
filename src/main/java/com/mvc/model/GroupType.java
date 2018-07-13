package com.mvc.model;

/**
 * Created by linchuanli on 2018/7/11.
 */
public enum GroupType {
    GET_UP("getUp"), WALK("walk");

    private String type;

    GroupType(String type) {
        this.type = type;
    }
    public String getType() {
        return type;
    }


}
