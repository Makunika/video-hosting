--liquibase formatted sql
--changeset maxim:6
ALTER TABLE videos
ADD COLUMN views numeric not null default 0