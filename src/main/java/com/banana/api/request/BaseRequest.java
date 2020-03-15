package com.banana.api.request;


import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class BaseRequest {
    private static final Logger LOGGER = LogManager.getLogger(BaseRequest.class.getName());
    private Integer offset = 0;
    private Integer limit = 20;

    public BaseRequest(String offset, String limit) {
        try {
            this.offset = Integer.valueOf(offset);
        } catch (Exception e) {
            LOGGER.debug("offset is null or not int", e);
        }
        try {
            this.limit = Integer.valueOf(limit);
        } catch (Exception e) {
            LOGGER.debug("limit is null or not int", e);
        }
    }

    public BaseRequest() {
    }

    public Integer getOffset() {
        return offset;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }
}
