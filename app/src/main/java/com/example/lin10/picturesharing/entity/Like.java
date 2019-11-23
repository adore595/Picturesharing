package com.example.lin10.picturesharing.entity;

import cn.bmob.v3.BmobObject;

public class Like extends BmobObject {
    private String username;
    private String imgobjectid;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getImgobjectid() {
        return imgobjectid;
    }

    public void setImgobjectid(String imgobjectid) {
        this.imgobjectid = imgobjectid;
    }
}
