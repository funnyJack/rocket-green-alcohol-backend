ALTER TABLE "user"
    ADD COLUMN referral_code VARCHAR(7) NOT NULL UNIQUE DEFAULT '',
    ADD COLUMN referrer_id   INTEGER;

ALTER TABLE "user"
    ADD CONSTRAINT fk_user_referrer
        FOREIGN KEY (referrer_id)
            REFERENCES "user" (id);
