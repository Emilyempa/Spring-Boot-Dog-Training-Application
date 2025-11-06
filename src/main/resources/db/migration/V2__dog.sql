CREATE TABLE dog (
    id INT AUTO_INCREMENT NOT NULL,
    name VARCHAR(255) NULL,
    breed VARCHAR(255) NULL,
    birthdate DATE NULL,
    owner_id INT NULL,
    CONSTRAINT pk_dog PRIMARY KEY (id),
    CONSTRAINT fk_dog_owner FOREIGN KEY (owner_id) REFERENCES users(id)
);