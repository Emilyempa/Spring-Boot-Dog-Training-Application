package org.example;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("api")
public class DogTrainingController {

    @GetMapping("/dogtraining")
    public String dogTraining() {
        return "Dog Training";
    }
}
