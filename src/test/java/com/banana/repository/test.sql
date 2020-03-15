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

INSERT INTO public."owner"
(id, user_name)
VALUES('25034321@N05', '');
INSERT INTO public."owner"
(id, user_name)
VALUES('153514133@N04', '');


INSERT INTO public.safe_annotation
(id, adult, spoof, medical, violence, racy)
VALUES(50, 1, 1, 1, 2, 1);
INSERT INTO public.safe_annotation
(id, adult, spoof, medical, violence, racy)
VALUES(51, 1, 2, 1, 2, 2);


INSERT INTO public.picture
(id, url, safe_annotation_id, owner_id)
VALUES('49659426712', 'https://flickr.com/photos/25034321@N05/49659426712', 50, '25034321@N05');
INSERT INTO public.picture
(id, url, safe_annotation_id, owner_id)
VALUES('49658562848', 'https://flickr.com/photos/153514133@N04/49658562848', 51, '153514133@N04');


INSERT INTO public.label_annotation
( "name", score, picture_id)
VALUES( 'Cat', 0.99504685, '49659426712');
INSERT INTO public.label_annotation
( "name", score, picture_id)
VALUES( 'Small to medium-sized cats', 0.97348434, '49659426712');
INSERT INTO public.label_annotation
( "name", score, picture_id)
VALUES( 'Whiskers', 0.9645563, '49659426712');
INSERT INTO public.label_annotation
( "name", score, picture_id)
VALUES('Felidae', 0.962594, '49659426712');
INSERT INTO public.label_annotation
( "name", score, picture_id)
VALUES( 'Black-and-white', 0.96189374, '49659426712');
INSERT INTO public.label_annotation
("name", score, picture_id)
VALUES( 'Tabby cat', 0.9544255, '49659426712');
INSERT INTO public.label_annotation
("name", score, picture_id)
VALUES( 'European shorthair', 0.90386665, '49659426712');
INSERT INTO public.label_annotation
("name", score, picture_id)
VALUES( 'Sleep', 0.8853391, '49659426712');
INSERT INTO public.label_annotation
( "name", score, picture_id)
VALUES( 'Monochrome', 0.85734415, '49659426712');
INSERT INTO public.label_annotation
( "name", score, picture_id)
VALUES('Nap', 0.84300876, '49659426712');
INSERT INTO public.label_annotation
("name", score, picture_id)
VALUES( 'Cat', 0.98893553, '49658562848');
INSERT INTO public.label_annotation
( "name", score, picture_id)
VALUES( 'White', 0.9705721, '49658562848');
INSERT INTO public.label_annotation
("name", score, picture_id)
VALUES( 'Black', 0.95355535, '49658562848');
INSERT INTO public.label_annotation
( "name", score, picture_id)
VALUES( 'Small to medium-sized cats', 0.94788975, '49658562848');
INSERT INTO public.label_annotation
( "name", score, picture_id)
VALUES( 'Felidae', 0.94647914, '49658562848');
INSERT INTO public.label_annotation
( "name", score, picture_id)
VALUES( 'Black-and-white', 0.93902534, '49658562848');
INSERT INTO public.label_annotation
("name", score, picture_id)
VALUES('Whiskers', 0.9125112, '49658562848');
INSERT INTO public.label_annotation
( "name", score, picture_id)
VALUES('Monochrome photography', 0.8539845, '49658562848');
INSERT INTO public.label_annotation
( "name", score, picture_id)
VALUES('Burmilla', 0.7906575, '49658562848');
INSERT INTO public.label_annotation
("name", score, picture_id)
VALUES( 'Monochrome', 0.75798035, '49658562848');


