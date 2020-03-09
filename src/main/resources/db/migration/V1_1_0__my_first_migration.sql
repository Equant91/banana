CREATE table if not exists owner
(
    id bigserial PRIMARY KEY,
    url VARCHAR(250)
);

CREATE table if not exists picture
(
    id bigserial PRIMARY KEY,
    url VARCHAR(250),
    owner_id BIGINT
        references owner
);

CREATE table if not exists label_annotation
(
    id bigserial PRIMARY KEY,
    name VARCHAR(100),
    score REAL ,
    picture_id BIGINT
            references picture
);

CREATE table if not exists safe_annotation

(
    id bigserial PRIMARY KEY,
    adult INTEGER,
    spoof INTEGER,
    medical INTEGER,
    violence INTEGER,
    racy  INTEGER,
    picture_id BIGINT
        references picture
);
