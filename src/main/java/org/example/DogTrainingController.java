package org.example;


import org.example.entities.DogTraining;
import org.example.errorhandling.DogTrainingNotFoundException;
import org.springframework.web.bind.annotation.*;


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
    public DogTrainingDTO create(@RequestBody DogTrainingDTO dto) {
        DogTraining saved = repository.save(new DogTraining(dto));
        return new DogTrainingDTO(saved);
    }
}
