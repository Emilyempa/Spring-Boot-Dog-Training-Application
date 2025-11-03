package org.example.controller;

import jakarta.validation.Valid;
import org.example.dto.DogTrainingRequestDTO;
import org.example.dto.DogTrainingResponseDTO;
import org.example.entities.Dog;
import org.example.entities.DogTraining;
import org.example.errorhandling.DogNotFoundException;
import org.example.errorhandling.DogTrainingNotFoundException;
import org.example.repository.DogRepository;
import org.example.repository.DogTrainingRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/dogtraining")
public class DogTrainingController {

    private final DogTrainingRepository repository;
    private final DogRepository dogRepository;

    public DogTrainingController(DogTrainingRepository repository, DogRepository dogRepository) {
        this.repository = repository;
        this.dogRepository = dogRepository;
    }

    @GetMapping
    public List<DogTrainingResponseDTO> getAll() {
        return repository.findAll().stream()
                .map(DogTrainingResponseDTO::new)
                .toList();
    }

    @GetMapping("/{id}")
    public DogTrainingResponseDTO getById(@PathVariable Integer id) {
        return repository.findById(id)
                .map(DogTrainingResponseDTO::new)
                .orElseThrow(() -> new DogTrainingNotFoundException(id));
    }

    @PostMapping
    public ResponseEntity<DogTrainingResponseDTO> create(
            @Valid @RequestBody DogTrainingRequestDTO dto) {

        Dog dog = dogRepository.findById(dto.dogId())
                .orElseThrow(() -> new DogNotFoundException(dto.dogId()));

        DogTraining training = new DogTraining();
        training.setActivity(dto.activity());
        training.setLocation(dto.location());
        training.setTrainingDate(dto.trainingDate());
        training.setDurationMinutes(dto.durationMinutes());
        training.setNotes(dto.notes());
        training.setDog(dog);

        DogTraining saved = repository.save(training);
        DogTrainingResponseDTO response = new DogTrainingResponseDTO(saved);

        return ResponseEntity
                .created(URI.create("/api/dogtraining/" + saved.getId()))
                .body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DogTrainingResponseDTO> update(
            @PathVariable Integer id,
            @Valid @RequestBody DogTrainingRequestDTO dto) {


        DogTraining existing = repository.findById(id)
                .orElseThrow(() -> new DogTrainingNotFoundException(id));

        Dog dog = dogRepository.findById(dto.dogId())
                .orElseThrow(() -> new DogNotFoundException(dto.dogId()));

        // Uppdate all fields
        existing.setActivity(dto.activity());
        existing.setLocation(dto.location());
        existing.setTrainingDate(dto.trainingDate());
        existing.setDurationMinutes(dto.durationMinutes());
        existing.setNotes(dto.notes());
        existing.setDog(dog);

        DogTraining updated = repository.save(existing);
        DogTrainingResponseDTO response = new DogTrainingResponseDTO(updated);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        DogTraining existing = repository.findById(id)
                .orElseThrow(() -> new DogTrainingNotFoundException(id));

        repository.delete(existing);
        return ResponseEntity.noContent().build();
    }
}