package org.example;

import org.example.entities.DogTraining;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class DogTrainingController {

    private final DogTrainingRepository repository;

    public DogTrainingController(DogTrainingRepository repository) {
        this.repository = repository;
    }

    // GET /api → welcome message
    @GetMapping
    public String welcome() {
        return "Welcome to Dog Training tracker";
    }

    // GET /api/dogtraining → get all sessions
    @GetMapping("/dogtraining")
    public List<DogTraining> getAllTrainings() {
        return repository.findAll();
    }

    // GET /api/dogtraining/{id} → get session by id
    @GetMapping("/dogtraining/{id}")
    public DogTraining getTrainingById(@PathVariable Integer id) {
        return repository.findById(id).orElseThrow();
    }

    // POST /api/dogtraining → create session
    @PostMapping("/dogtraining")
    public DogTraining createTraining(@RequestBody DogTraining training) {
        return repository.save(training);
    }
}
