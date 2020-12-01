--liquibase formatted sql
--changeset maxim:2

INSERT INTO Users (name, password_hash, token, email)
VALUES('Maxim', '123', '123', '123');

INSERT INTO Users (name, password_hash, token, email)
VALUES('Maxim2', '123', '123', '123');

INSERT INTO Users (name, password_hash, token, email)
VALUES('Maxim3', '123', '123', '123');

INSERT INTO Users (name, password_hash, token, email)
VALUES('Maxim4', '123', '123', '123');

INSERT INTO Videos (video, user_id)
VALUES('video.mp4', 1);

INSERT INTO Videos (user_id)
VALUES(3);