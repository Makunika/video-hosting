--liquibase formatted sql
--changeset maxim:7
CREATE TABLE marks
(
    id              serial              PRIMARY KEY,
    mark            int                 NOT NULL DEFAULT 0,
    user_id         int                 NOT NULL,
    video_id        varchar(36)         NOT NULL,
    created         timestamp           NOT NULL DEFAULT NOW(),
    updated         timestamp           NOT NULL DEFAULT NOW(),

    CONSTRAINT fk_comments_videos FOREIGN KEY (user_id) REFERENCES users (id)
        ON UPDATE CASCADE
        ON DELETE CASCADE,

    CONSTRAINT fk_comments_users FOREIGN KEY (video_id) REFERENCES videos (id)
        ON UPDATE CASCADE
        ON DELETE CASCADE
)
