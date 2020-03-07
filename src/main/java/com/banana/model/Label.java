package com.banana.model;

import org.javalite.activejdbc.Model;

public class Label extends Model {
    public String getName() {
        return getString("name");
    }

    public void getName(String name) {
        set("name", name);
    }

    public Double getScore() {
        return getDouble("score");
    }

    public void getName(Double score) {
        set("score", score);
    }
}
