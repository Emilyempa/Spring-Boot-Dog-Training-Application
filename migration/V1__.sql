CREATE DATABASE IF NOT EXISTS dog_training_db;
USE dog_training_db;

CREATE TABLE dog_training
(
    id               INT AUTO_INCREMENT NOT NULL,
    activity         VARCHAR(255) NULL,
    location         VARCHAR(255) NULL,
    training_date    date NULL,
    duration_minutes INT NOT NULL,
    notes            LONGTEXT NULL,
    created_at       datetime NULL,
    CONSTRAINT pk_dogtraining PRIMARY KEY (id)
);