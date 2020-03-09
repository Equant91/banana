package com.banana.model;

import com.google.protobuf.DescriptorProtos;
import org.javalite.activejdbc.LazyList;
import org.javalite.activejdbc.Model;

public class Owner extends Model {

    public LazyList<Picture> getPicture() {
        return getAll(Picture.class);
    }

    public void addPicture(Picture picture) {
        add(picture);
    }

    public void setPicture(LazyList<Picture> pictures) {
        set(pictures);
    }
}
