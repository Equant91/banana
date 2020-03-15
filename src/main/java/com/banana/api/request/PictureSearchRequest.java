package com.banana.api.request;

public class PictureSearchRequest extends BaseRequest {
    private String owner;
    private String label;

    public PictureSearchRequest(String owner, String label, String offset, String limit) {
        super(offset, limit);
        this.owner = owner;
        this.label = label;
    }

    public PictureSearchRequest() {
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
