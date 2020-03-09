package com.banana.model;

import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.Table;

@Table("label_annotation")
public class LabelAnnotation extends Model {
    public String getName() {
        return getString("kab");
    }

    public void setName(String name) {
        set("name", name);
    }

    public Double getScore() {
        return getDouble("score");
    }

    public void setScore(Double score) {
        set("score", score);
    }
}
