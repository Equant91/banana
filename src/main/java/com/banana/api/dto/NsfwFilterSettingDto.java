package com.banana.api.dto;

public class NsfwFilterSettingDto {
    private Integer adult;
    private Integer spoof;
    private Integer medical;
    private Integer violence;
    private Integer racy;

    public Integer getAdult() {
        return adult;
    }

    public void setAdult(Integer adult) {
        this.adult = adult;
    }

    public Integer getSpoof() {
        return spoof;
    }

    public void setSpoof(Integer spoof) {
        this.spoof = spoof;
    }

    public Integer getMedical() {
        return medical;
    }

    public void setMedical(Integer medical) {
        this.medical = medical;
    }

    public Integer getViolence() {
        return violence;
    }

    public void setViolence(Integer violence) {
        this.violence = violence;
    }

    public Integer getRacy() {
        return racy;
    }

    public void setRacy(Integer racy) {
        this.racy = racy;
    }
}
