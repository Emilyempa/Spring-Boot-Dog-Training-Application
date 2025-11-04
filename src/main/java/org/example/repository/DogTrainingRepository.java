package org.example.repository;

import org.example.entities.DogTraining;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DogTrainingRepository extends JpaRepository<DogTraining, Integer> {

    List<DogTraining> findByDogId(Integer dogId);

    List<DogTraining> findByDogIdAndActivity(Integer dogId, String activity);
}

