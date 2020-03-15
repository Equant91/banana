CREATE table if not exists nsfw_filter_setting
(
    id         bigserial PRIMARY KEY,
    adult      INTEGER,
    spoof      INTEGER,
    medical    INTEGER,
    violence   INTEGER,
    racy       INTEGER
);

Insert into nsfw_filter_setting values (1,3,3,3,3,3);