package org.example.controller;

import jakarta.validation.Valid;
import org.example.dto.DogRequestDTO;
import org.example.dto.DogResponseDTO;
import org.example.entities.Dog;
import org.example.errorhandling.DogNotFoundException;
import org.example.repository.DogRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("api/dog")
public class DogController {

    private final DogRepository repository;

    public DogController(DogRepository repository) {
        this.repository = repository;
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
