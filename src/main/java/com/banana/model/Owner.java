package com.banana.model;

import org.javalite.activejdbc.LazyList;
import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.Table;

@Table("owner")
public class Owner extends Model {

    public LazyList<Picture> getPictures() {
        return getAll(Picture.class);
    }

    public void addPicture(Picture picture) {
        add(picture);
    }

    public void setPicture(LazyList<Picture> pictures) {
        set(pictures);
    }

    public String getUserName() {
        return getString("user_name");
    }

    public void setUserName(String userName) {
        set("user_name", userName);
    }
}
