package com.banana.utils;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class PictureSqlBuilder {
    private static final Logger LOGGER = LogManager.getLogger(PictureSqlBuilder.class.getName());

    private final String FIND_ALL_PICTURE = "Select distinct picture.* from picture " +
            "inner join label_annotation as la on picture.id = la.picture_id " +
            "inner join safe_annotation as safe on picture.safe_annotation_id = safe.id ";
    private final String WITH_NSFW_FILTER = " safe.adult <= ? " +
            "and safe.medical <= ? " +
            "and safe.racy <= ? " +
            "and safe.spoof <= ? " +
            "and safe.violence <= ? ";
    private final String LIMIT = " limit ";
    private final String OFFSET = " offset ";
    private Integer offset = 0;
    private Integer limit = 20;
    private StringBuilder queryBuilder;

    private boolean hasQuery;

    public PictureSqlBuilder() {
        this.queryBuilder = new StringBuilder(FIND_ALL_PICTURE);
    }

    public String build() {
        String query = queryBuilder
                .append(hasQuery ? " and " : " where ")
                .append(WITH_NSFW_FILTER)
                .append(LIMIT).append(limit)
                .append(OFFSET).append(offset)
                .toString();
        LOGGER.info(query);
        return query;
    }

    public PictureSqlBuilder where(Criteria criteria, String query) {
        if (hasQuery) {
            queryBuilder.append(" and ");
        } else {
            queryBuilder.append(" where ");
        }
        hasQuery = true;
        queryBuilder.append(criteria.getValue() + "\'" + query + "\'");
        return this;
    }

    public PictureSqlBuilder offset(Integer offset) {
        this.offset = offset;
        return this;
    }

    public PictureSqlBuilder limit(Integer limit) {
        this.limit = limit;
        return this;
    }

    public enum Criteria {

        OWNER(" picture.owner_id  = "),
        LABEL(" la.name = ");

        private final String value;

        Criteria(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }
}
