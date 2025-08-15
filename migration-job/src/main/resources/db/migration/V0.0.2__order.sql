CREATE TYPE contract_type AS ENUM (
    'HYDROGEN_FRIENDS',
    'CONTRACTED_OWNER',
    'GREEN_ALCOHOL_PIONEER',
    'GREEN_ALCOHOL_PARTNERS'
);

CREATE TABLE "order"
(
    id            SERIAL PRIMARY KEY,
    user_id        INTEGER NOT NULL REFERENCES "user"(id),
    contract_type contract_type NOT NULL
);
