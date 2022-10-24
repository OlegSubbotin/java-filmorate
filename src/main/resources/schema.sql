CREATE TABLE IF NOT EXISTS film
(
    id          bigint GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name        varchar,
    description varchar,
    releaseDate date,
    duration    bigint,
    rating      varchar
);

CREATE TABLE IF NOT EXISTS users
(
    id       bigint GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    email    varchar,
    login    varchar,
    name     varchar,
    birthday timestamp
);

CREATE TABLE IF NOT EXISTS genre
(
    id   bigint GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name varchar
);

CREATE TABLE IF NOT EXISTS film_genre
(
    film_id  bigint,
    genre_id bigint,
    CONSTRAINT pk_fg PRIMARY KEY (film_id, genre_id),
    CONSTRAINT fk_fg_film FOREIGN KEY (film_id) REFERENCES film (id),
    CONSTRAINT fk_fg_genre FOREIGN KEY (genre_id) REFERENCES genre (id)
);

CREATE TABLE IF NOT EXISTS likes
(
    film_id bigint,
    user_id bigint,
    CONSTRAINT pk_likes PRIMARY KEY (film_id, user_id),
    CONSTRAINT fk_likes_film FOREIGN KEY (film_id) REFERENCES film (id),
    CONSTRAINT fk_likes_user FOREIGN KEY (user_id) REFERENCES users (id)
);

CREATE TABLE IF NOT EXISTS friends
(
    user_id    bigint,
    friends_id bigint,
    status     boolean,
    CONSTRAINT pk_friends PRIMARY KEY (user_id, friends_id),
    CONSTRAINT fk_friends_user FOREIGN KEY (user_id) REFERENCES users (id),
    CONSTRAINT fk_friends_friend FOREIGN KEY (friends_id) REFERENCES users (id)
);