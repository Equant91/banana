CREATE table if not exists owner
(
    id        VARCHAR(100) PRIMARY KEY,
    user_name VARCHAR(50)
);

CREATE table if not exists safe_annotation

(
    id         bigserial PRIMARY KEY,
    adult      INTEGER,
    spoof      INTEGER,
    medical    INTEGER,
    violence   INTEGER,
    racy       INTEGER
);

CREATE table if not exists picture
(
    id                 VARCHAR(100) PRIMARY KEY,
    url                VARCHAR(250),
    safe_annotation_id bigint
        references safe_annotation,
    owner_id           VARCHAR(100)
        references owner
);

CREATE table if not exists label_annotation
(
    id         bigserial PRIMARY KEY,
    name       VARCHAR(100),
    score      REAL,
    picture_id VARCHAR(100)
        references picture
);

