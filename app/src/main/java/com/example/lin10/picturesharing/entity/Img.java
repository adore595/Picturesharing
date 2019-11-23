package com.example.lin10.picturesharing.entity;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

public class Img extends BmobObject {
    private BmobFile image;
    private String author;


    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getImageurl() {
        return image.getUrl();
    }

    public void setImage(BmobFile image) {
        this.image = image;
    }
}
