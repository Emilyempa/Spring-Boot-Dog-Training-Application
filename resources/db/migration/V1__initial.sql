CREATE TABLE dog
(
    id        INT AUTO_INCREMENT NOT NULL,
    name      VARCHAR(50)        NOT NULL,
    breed     VARCHAR(50)        NOT NULL,
    birthdate date               NOT NULL,
    owner_id  INT                NULL,
    CONSTRAINT pk_dog PRIMARY KEY (id)
);

CREATE TABLE dog_training
(
    id               INT AUTO_INCREMENT NOT NULL,
    activity         VARCHAR(100)       NOT NULL,
    location         VARCHAR(100)       NOT NULL,
    training_date    date               NOT NULL,
    duration_minutes INT                NOT NULL,
    notes            TEXT               NULL,
    dog_id           INT                NULL,
    created_at       datetime           NOT NULL,
    CONSTRAINT pk_dogtraining PRIMARY KEY (id)
);

CREATE TABLE users
(
    id       INT AUTO_INCREMENT NOT NULL,
    username VARCHAR(255)       NOT NULL,
    password VARCHAR(255)       NOT NULL,
    `role`   VARCHAR(255)       NOT NULL,
    enabled  BIT(1)             NOT NULL,
    CONSTRAINT pk_users PRIMARY KEY (id)
);

ALTER TABLE users
    ADD CONSTRAINT uc_users_username UNIQUE (username);

ALTER TABLE dog_training
    ADD CONSTRAINT FK_DOGTRAINING_ON_DOG FOREIGN KEY (dog_id) REFERENCES dog (id);

ALTER TABLE dog
    ADD CONSTRAINT FK_DOG_ON_OWNER FOREIGN KEY (owner_id) REFERENCES users (id);