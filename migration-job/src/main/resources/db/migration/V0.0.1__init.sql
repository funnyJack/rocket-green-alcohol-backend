CREATE TABLE "user"
(
    id           SERIAL PRIMARY KEY,
    openid       TEXT NOT NULL UNIQUE,
    avatar_url   TEXT,
    nickname     VARCHAR(255),
    phone_number VARCHAR(255),
    address      TEXT
);
