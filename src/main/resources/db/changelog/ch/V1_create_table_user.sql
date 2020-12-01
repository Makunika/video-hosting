--liquibase formatted sql
--changeset maxim:1
CREATE TABLE Users
(
    id SERIAL PRIMARY KEY,
    name varchar(100) NOT NULL,
    password_hash varchar(255) NOT NULL,
    token varchar(255),
    email varchar(100) NOT NULL,
    img varchar(255) NOT NULL DEFAULT 'default.png'
)