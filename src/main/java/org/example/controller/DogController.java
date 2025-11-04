package org.example.controller;

import jakarta.validation.Valid;
import org.example.dto.DogRequestDTO;
import org.example.dto.DogResponseDTO;
import org.example.dto.DogTrainingResponseDTO;
import org.example.entities.Dog;
import org.example.entities.DogTraining;
import org.example.errorhandling.DogNotFoundException;
import org.example.repository.DogRepository;
import org.example.repository.DogTrainingRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("api/dog")
public class DogController {

    private final DogRepository repository;
    private final DogTrainingRepository dogTrainingRepository;

    public DogController(DogRepository repository, DogTrainingRepository dogTrainingRepository) {
        this.repository = repository;
        this.dogTrainingRepository = dogTrainingRepository;
    }

    @GetMapping
    public List<DogResponseDTO> getAll() {
        return repository.findAll().stream()
                .map(DogResponseDTO::new)
                .toList();
    }

    @GetMapping("/{id}")
    public DogResponseDTO getById(@PathVariable Integer id) {
        return repository.findById(id)
                .map(DogResponseDTO::new)
                .orElseThrow(() -> new DogNotFoundException(id));
    }

    @GetMapping("/{id}/dogtraining")
    public List<DogTrainingResponseDTO> getDogTrainings(
            @PathVariable Integer id,
            @RequestParam(required = false) String activity) {

        List<DogTraining> training;

        if (activity != null && !activity.isBlank()) {
            training = dogTrainingRepository.findByDogIdAndActivity(id, activity);
        } else {
            training = dogTrainingRepository.findByDogId(id);
        }

        return training.stream()
                .map(DogTrainingResponseDTO::new)
                .toList();

    }

    @PostMapping
    public ResponseEntity<DogResponseDTO> create(@Valid @RequestBody DogRequestDTO dto) {
        Dog dog = new Dog();
        dog.setName(dto.name());
        dog.setBreed(dto.breed());
        dog.setBirthdate(dto.birthdate());

        Dog saved = repository.save(dog);
        DogResponseDTO response = new DogResponseDTO(saved);

        return ResponseEntity
                .created(URI.create("/api/dogs/" + saved.getId()))
                .body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DogResponseDTO> update(
            @PathVariable Integer id,
            @Valid @RequestBody DogRequestDTO dto) {

        Dog existing = repository.findById(id)
                .orElseThrow(() -> new DogNotFoundException(id));

        existing.setName(dto.name());
        existing.setBreed(dto.breed());
        existing.setBirthdate(dto.birthdate());

        Dog updated = repository.save(existing);
        DogResponseDTO response = new DogResponseDTO(updated);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        Dog existing = repository.findById(id)
                .orElseThrow(() -> new DogNotFoundException(id));

        repository.delete(existing);
        return ResponseEntity.noContent().build();
    }
}
