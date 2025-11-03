package org.example.repository;

import org.example.entities.DogTraining;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DogTrainingRepository extends JpaRepository<DogTraining, Integer> { }

