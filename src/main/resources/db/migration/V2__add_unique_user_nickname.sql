ALTER TABLE user
    ADD CONSTRAINT uc_user_nickname UNIQUE (nickname);