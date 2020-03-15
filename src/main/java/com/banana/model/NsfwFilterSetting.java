package com.banana.model;

import org.javalite.activejdbc.LazyList;
import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.Table;

@Table("nsfw_filter_setting")
public class NsfwFilterSetting extends Model {
    public static NsfwFilterSetting getSetting() {
        LazyList<NsfwFilterSetting> all = findAll();
        if (all == null && all.isEmpty()) {
            throw new RuntimeException("Не заданы настройки NsfwFilterSetting");
        }
        return all.get(0);
    }

    public Integer getAdult() {
        return getInteger("adult");
    }

    public void setAdult(Integer adult) {
        set("adult", adult);
    }

    public Integer getSpoof() {
        return getInteger("spoof");
    }

    public void setSpoof(Integer spoof) {
        set("spoof", spoof);
    }

    public Integer getMedical() {
        return getInteger("medical");
    }

    public void setMedical(Integer medical) {
        set("medical", medical);
    }

    public Integer getViolence() {
        return getInteger("violence");
    }

    public void setViolence(Integer violence) {
        set("violence", violence);
    }

    public Integer getRacy() {
        return getInteger("racy");
    }

    public void setRacy(Integer racy) {
        set("racy", racy);
    }
}
