package com.banana.model;

import org.javalite.activejdbc.LazyList;
import org.javalite.activejdbc.Model;

public class Picture extends Model {

    public LazyList<Label> getLabel() {
        return getAll(Label.class);
    }

    public void addLabel(Label label) {
        add(label);
    }
    public void setLabel( LazyList<Label>  labels) {
        set(labels);
    }
}
