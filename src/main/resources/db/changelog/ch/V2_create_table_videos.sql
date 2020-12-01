--liquibase formatted sql
--changeset maxim:2
CREATE TABLE Videos
(
    id SERIAL PRIMARY KEY,
    video varchar(255) NOT NULL DEFAULT 'default.mp4',
    user_id INT NOT NULL,

    CONSTRAINT fk_users_videos FOREIGN KEY (user_id) REFERENCES users (id)
        ON UPDATE CASCADE
        ON DELETE CASCADE
)