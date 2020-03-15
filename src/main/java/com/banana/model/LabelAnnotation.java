package com.banana.model;

import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.BelongsTo;
import org.javalite.activejdbc.annotations.Table;

@Table("label_annotation")
@BelongsTo(parent = Picture.class, foreignKeyName = "picture_id")
public class LabelAnnotation extends Model {
    public String getName() {
        return getString("name");
    }

    public void setName(String name) {
        set("name", name);
    }

    public Float getScore() {
        return getFloat("score");
    }

    public void setScore(Float score) {
        set("score", score);
    }
}
