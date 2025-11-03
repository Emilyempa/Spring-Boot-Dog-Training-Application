package org.example.repository;

import org.example.entities.Dog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DogRepository extends JpaRepository <Dog, Integer>{
}
