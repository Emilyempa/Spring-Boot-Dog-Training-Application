CREATE TABLE dog
(
    id        INT AUTO_INCREMENT NOT NULL,
    name      VARCHAR(255)       NULL,
    breed     VARCHAR(255)       NULL,
    birthdate date               NULL,
    CONSTRAINT pk_dog PRIMARY KEY (id)
);

CREATE TABLE dog_training
(
    id               INT AUTO_INCREMENT NOT NULL,
    activity         VARCHAR(255)       NULL,
    location         VARCHAR(255)       NULL,
    training_date    date               NULL,
    duration_minutes INT                NOT NULL,
    notes            LONGTEXT           NULL,
    dog_id           INT                NULL,
    created_at       datetime           NULL,
    CONSTRAINT pk_dogtraining PRIMARY KEY (id)
);

ALTER TABLE dog_training
    ADD CONSTRAINT FK_DOGTRAINING_ON_DOG FOREIGN KEY (dog_id) REFERENCES dog (id);