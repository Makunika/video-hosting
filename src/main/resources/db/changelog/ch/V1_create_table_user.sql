--liquibase formatted sql
--changeset maxim:1
CREATE TABLE Games
(
    id SERIAL PRIMARY KEY,
    name varchar(50) NOT NULL
)