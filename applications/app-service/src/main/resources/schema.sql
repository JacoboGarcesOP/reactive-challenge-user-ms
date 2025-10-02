CREATE SCHEMA IF NOT EXISTS user_schema;

CREATE TABLE IF NOT EXISTS user_schema.user (
    user_id BIGSERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    email VARCHAR(50) NOT NULL
);

CREATE TABLE IF NOT EXISTS user_schema.user_bootcamp (
    id BIGSERIAL PRIMARY KEY
    user_id BIGINT,
    bootcamp_id BIGINT NOT NULL,
    UNIQUE(bootcamp_id, user_id),
    FOREIGN KEY (user_id) REFERENCES user_schema.user(user_id) ON DELETE CASCADE
);

CREATE INDEX idx_user_bootcamp_user_id ON user_schema.user_bootcamp(user_id);