CREATE SCHEMA IF NOT EXISTS app;

CREATE TABLE IF NOT EXISTS app.genres(
    id BIGSERIAL,
    name VARCHAR(30),
    CONSTRAINT pk_genre_id PRIMARY KEY (id)
);

CREATE TABLE app.artist (
    id BIGSERIAL NOT NULL,
    name TEXT NOT NULL,
    CONSTRAINT pk_artist_id PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS app.votes(
    id BIGSERIAL,
    artist_id INT NOT NULL,
    about TEXT NOT NULL,
    creation_time TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    CONSTRAINT pk_vote_id PRIMARY KEY (id),
    CONSTRAINT fk_artist_id FOREIGN KEY (artist_id)
    REFERENCES app.artist (id)
);

CREATE TABLE IF NOT EXISTS app.votes_genres(
    vote_id BIGINT NOT NULL,
    genre_id INT NOT NULL,
    CONSTRAINT fk_vote_id FOREIGN KEY (vote_id)
    REFERENCES app.votes (id),
    CONSTRAINT fk_genre_id FOREIGN KEY (genre_id)
    REFERENCES app.genres (id),
    CONSTRAINT unique_vote UNIQUE(vote_id, genre_id)
);

