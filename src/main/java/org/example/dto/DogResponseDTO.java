package org.example.dto;

import org.example.entities.Dog;

import java.time.LocalDate;

public record DogResponseDTO(
        Integer id,
        String name,
        String breed,
        LocalDate birthdate
) {
    // Constructorthat converts from Entity to DTO
    public DogResponseDTO(Dog entity) {
        this(
                entity.getId(),
                entity.getName(),
                entity.getBreed(),
                entity.getBirthdate()
        );
    }
}
