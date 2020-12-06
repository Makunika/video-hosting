--liquibase formatted sql
--changeset maxim:4

INSERT INTO users (name, password_hash, token, email)
VALUES('Maxim', '123', '123', '123');

INSERT INTO users (name, password_hash, token, email)
VALUES('Maxim2', '123', '123', '123');

INSERT INTO users (name, password_hash, token, email)
VALUES('Maxim3', '123', '123', '123');

INSERT INTO users (name, password_hash, token, email)
VALUES('Maxim4', '123', '123', '123');

INSERT INTO videos (id, name, about, video, user_id)
VALUES ('f6d7692a-c4a8-4489-a408-b93b9ad75e39', 'Видосик', 'Об этом видео можно скзаать чоень многое и не только!' , 'video.mp4', 1);

INSERT INTO videos (id, name, video, user_id)
VALUES ('68727050-a833-4097-bf36-dd9c1408299b', 'Нерабочий видосик', 'default.mp4', 1);
