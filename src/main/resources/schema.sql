DROP TABLE links IF EXISTS;
DROP TABLE redirects IF EXISTS;
DROP TABLE users IF EXISTS;


CREATE TABLE users
(
    id               SERIAL PRIMARY KEY,
    secret_key       VARCHAR(255)           NOT NULL
);

CREATE TABLE links
(
    id           SERIAL PRIMARY KEY,
    original_url    VARCHAR(255) NOT NULL,
    short_url       VARCHAR(255) NOT NULL,
    created_to      TIMESTAMP            ,
    user_id         INTEGER      NOT NULL,
    FOREIGN KEY (user_id) REFERENCES USERS (id) ON DELETE CASCADE
);

CREATE TABLE redirects
(
    id              SERIAL PRIMARY KEY,
    time_redirect   TIMESTAMP DEFAULT now() NOT NULL,
    url             VARCHAR(255)            NOT NULL,
    user_id         INTEGER                 NOT NULL,
    FOREIGN KEY (user_id) REFERENCES USERS (id) ON DELETE CASCADE
);