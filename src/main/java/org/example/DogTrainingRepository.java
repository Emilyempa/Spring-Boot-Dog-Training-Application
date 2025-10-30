package org.example;

import org.example.entities.DogTraining;
import org.springframework.data.repository.ListCrudRepository;

public interface DogTrainingRepository extends ListCrudRepository<DogTraining, Integer> { }

