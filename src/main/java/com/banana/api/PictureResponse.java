package com.banana.api;

import java.util.List;

public class PictureResponse {
    String id;
    String url;
    SafeAnnotationResponse safeAnnotationResponse;
    List<LabelAnnotationResponse> labelAnnotationResponses;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public SafeAnnotationResponse getSafeAnnotationResponse() {
        return safeAnnotationResponse;
    }

    public void setSafeAnnotationResponse(SafeAnnotationResponse safeAnnotationResponse) {
        this.safeAnnotationResponse = safeAnnotationResponse;
    }

    public List<LabelAnnotationResponse> getLabelAnnotationResponses() {
        return labelAnnotationResponses;
    }

    public void setLabelAnnotationResponses(List<LabelAnnotationResponse> labelAnnotationResponses) {
        this.labelAnnotationResponses = labelAnnotationResponses;
    }
}
