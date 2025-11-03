package org.example.dto;

import org.example.entities.DogTraining;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record DogTrainingResponseDTO(
        Integer id,
        String activity,
        String location,
        LocalDate trainingDate,
        int durationMinutes,
        String notes,
        Integer dogId,
        LocalDateTime createdAt
) {
    // Constructor that converts from Entity to DTO
    public DogTrainingResponseDTO(DogTraining entity) {
        this(
                entity.getId(),
                entity.getActivity(),
                entity.getLocation(),
                entity.getTrainingDate(),
                entity.getDurationMinutes(),
                entity.getNotes(),
                entity.getDog() != null ? entity.getDog().getId() : null,
                entity.getCreatedAt()
        );
    }
}