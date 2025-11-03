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
@RequestMapping("api/dogtraining")
public class DogTrainingController {

    private final DogTrainingRepository repository;

    public DogTrainingController(DogTrainingRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<DogTrainingDTO> getAll() {
        return repository.findAll().stream()
                .map(DogTrainingDTO::new)
                .toList();
    }

    @GetMapping("/{id}")
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
}
