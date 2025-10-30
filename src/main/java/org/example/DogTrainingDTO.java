package org.example;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record DogTrainingDTO(
        String activity,
        String location,
        LocalDate trainingDate,
        int durationMinutes,
        String notes,
        LocalDateTime createdAt
) {
    // constructor that builds DTO from entity
    public DogTrainingDTO(org.example.entities.DogTraining entity) {
        this(
                entity.getActivity(),
                entity.getLocation(),
                entity.getTrainingDate(),
                entity.getDurationMinutes(),
                entity.getNotes(),
                entity.getCreatedAt()
        );
    }
}
