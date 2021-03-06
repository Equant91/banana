package com.banana.model;

import org.javalite.activejdbc.LazyList;
import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.IdName;
import org.javalite.activejdbc.annotations.Table;

@Table("picture")
@IdName("id")
public class Picture extends Model {

    public LazyList<LabelAnnotation> getLabel() {
        return getAll(LabelAnnotation.class);
    }

    public void setLabel(LazyList<LabelAnnotation> labelAnnotations) {
        set(labelAnnotations);
    }

    public void addLabel(LabelAnnotation labelAnnotation) {
        add(labelAnnotation);
    }

    public SafeAnnotation getSafeAnnotation() {
        return SafeAnnotation.findById(getSafeAnnotationId());
    }

    public void setSafeAnnotation(Long id) {
        set("safe_annotation_id", id);
    }

    public void setSafeAnnotation(SafeAnnotation setSafeAnnotation) {
        setSafeAnnotation(setSafeAnnotation.getLongId());
    }

    private Long getSafeAnnotationId() {
        return getLong("safe_annotation_id");
    }

    public String getUrl() {
        return getString("url");
    }

    public void setUrl(String url) {
        set("url", url);
    }

    public Owner getOwner() {
        return Owner.findById(getString("owner_id"));
    }

}
