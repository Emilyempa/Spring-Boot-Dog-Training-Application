CREATE TABLE dog_training (
    id INT AUTO_INCREMENT NOT NULL,
    activity VARCHAR(255) NULL,
    location VARCHAR(255) NULL,
    training_date DATE NULL,
    duration_minutes INT NOT NULL,
    notes LONGTEXT NULL,
    dog_id INT NULL,
    created_at DATETIME NULL,
    CONSTRAINT pk_dogtraining PRIMARY KEY (id),
    CONSTRAINT fk_dogtraining_on_dog FOREIGN KEY (dog_id) REFERENCES dog (id)
);