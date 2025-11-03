package org.example.controller;


import jakarta.validation.Valid;
import org.example.dto.DogTrainingDTO;
import org.example.repository.DogTrainingRepository;
import org.example.entities.DogTraining;
import org.example.errorhandling.DogTrainingNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("api")
public class DogTrainingController {

    private final DogTrainingRepository repository;

    public DogTrainingController(DogTrainingRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/dogtraining")
    public List<DogTrainingDTO> getAll() {
        return repository.findAll().stream()
                .map(DogTrainingDTO::new)
                .toList();
    }

    @GetMapping("/dogtraining/{id}")
    public DogTrainingDTO getById(@PathVariable Integer id) {
        return repository.findById(id)
                .map(DogTrainingDTO::new)
                .orElseThrow(() -> new DogTrainingNotFoundException(id));
    }

    @PostMapping
    public ResponseEntity<DogTrainingDTO> create(@Valid @RequestBody DogTrainingDTO dto) {
        DogTraining saved = repository.save(new DogTraining(dto));
        DogTrainingDTO response = new DogTrainingDTO(saved);

        return ResponseEntity
                .created(URI.create("/api/dogtraining/" + saved.getId())) // gives Location-header
                .body(response);
        }
    @PutMapping("/dogtraining/{id}")
    public ResponseEntity<DogTrainingDTO> update(
            @PathVariable Integer id,
            @Valid @RequestBody DogTrainingDTO dto) {

        DogTraining existing = repository.findById(id)
                .orElseThrow(() -> new DogTrainingNotFoundException(id));

        existing.setActivity(dto.activity());
        existing.setLocation(dto.location());
        existing.setTrainingDate(dto.trainingDate());
        existing.setDurationMinutes(dto.durationMinutes());
        existing.setNotes(dto.notes());

        DogTraining updated = repository.save(existing);
        DogTrainingDTO response = new DogTrainingDTO(updated);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/dogtraining/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        DogTraining existing = repository.findById(id)
                .orElseThrow(() -> new DogTrainingNotFoundException(id));

        repository.delete(existing);
        return ResponseEntity.noContent().build();
    }
}
