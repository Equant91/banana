package com.banana.api.response;

import com.google.cloud.vision.v1.Likelihood;

public class SafeAnnotationResponse {
    String adult;
    String spoof;
    String medical;
    String violence;
    String racy;

    public String getAdult() {
        return adult;
    }

    public void setAdult(int adult) {
        this.adult = Likelihood.forNumber(adult).name();
    }

    public String getSpoof() {
        return spoof;
    }

    public void setSpoof(int spoof) {
        this.spoof = Likelihood.forNumber(spoof).name();
    }

    public String getMedical() {
        return medical;
    }

    public void setMedical(int medical) {
        this.medical = Likelihood.forNumber(medical).name();
    }

    public String getViolence() {
        return violence;
    }

    public void setViolence(int violence) {
        this.violence = Likelihood.forNumber(violence).name();
    }

    public String getRacy() {
        return racy;
    }

    public void setRacy(int racy) {
        this.racy = Likelihood.forNumber(racy).name();
    }
}
