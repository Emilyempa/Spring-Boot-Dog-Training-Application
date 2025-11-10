package org.example.service;

import org.example.dto.DogTrainingRequestDTO;
import org.example.dto.DogTrainingResponseDTO;
import org.example.entities.Dog;
import org.example.entities.DogTraining;
import org.example.entities.User;
import org.example.errorhandling.DogNotFoundException;
import org.example.errorhandling.DogTrainingNotFoundException;
import org.example.repository.DogRepository;
import org.example.repository.DogTrainingRepository;
import org.example.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DogTrainingService {

    private final DogTrainingRepository trainingRepository;
    private final DogRepository dogRepository;
    private final UserRepository userRepository;

    public DogTrainingService(DogTrainingRepository trainingRepository, DogRepository dogRepository, UserRepository userRepository) {
        this.trainingRepository = trainingRepository;
        this.dogRepository = dogRepository;
        this.userRepository = userRepository;
    }

    public List<DogTrainingResponseDTO> getAllTrainings(Authentication auth) {
        User currentUser = userRepository.findByUsername(auth.getName()).orElseThrow();

        List<DogTraining> trainings = currentUser.getRole().equals("ROLE_ADMIN")
                ? trainingRepository.findAll()
                : trainingRepository.findByDogOwnerId(currentUser.getId());

        return trainings.stream().map(DogTrainingResponseDTO::new).toList();
    }

    public DogTrainingResponseDTO getTrainingById(Integer id, Authentication auth) {
        User currentUser = userRepository.findByUsername(auth.getName()).orElseThrow();

        DogTraining training = currentUser.getRole().equals("ROLE_ADMIN")
                ? trainingRepository.findById(id)
                .orElseThrow(() -> new DogTrainingNotFoundException(id))
                : trainingRepository.findByIdAndDogOwnerId(id, currentUser.getId())
                .orElseThrow(() -> new DogTrainingNotFoundException(id));

        return new DogTrainingResponseDTO(training);
    }

    public DogTrainingResponseDTO createTraining(DogTrainingRequestDTO dto, Authentication auth) {
        User currentUser = userRepository.findByUsername(auth.getName()).orElseThrow();
        Dog dog = dogRepository.findById(dto.dogId())
                .orElseThrow(() -> new DogNotFoundException(dto.dogId()));

        // checks if dog has specifik owner (if not admin)
        if (!currentUser.getRole().equals("ROLE_ADMIN") && !dog.getOwner().getId().equals(currentUser.getId())) {
            throw new IllegalArgumentException("You cannot add training for a dog you do not own.");
        }

        DogTraining training = new DogTraining();
        training.setActivity(dto.activity());
        training.setLocation(dto.location());
        training.setTrainingDate(dto.trainingDate());
        training.setDurationMinutes(dto.durationMinutes());
        training.setNotes(dto.notes());
        training.setDog(dog);

        return new DogTrainingResponseDTO(trainingRepository.save(training));
    }

    public void deleteTraining(Integer id, Authentication auth) {
        User currentUser = userRepository.findByUsername(auth.getName()).orElseThrow();

        DogTraining training = currentUser.getRole().equals("ROLE_ADMIN")
                ? trainingRepository.findById(id).orElseThrow(() -> new DogTrainingNotFoundException(id))
                : trainingRepository.findByIdAndDogOwnerId(id, currentUser.getId())
                .orElseThrow(() -> new DogTrainingNotFoundException(id));

        trainingRepository.delete(training);
    }

    // Get all trainings for a specific dog
    public List<DogTrainingResponseDTO> getTrainingsByDogId(Integer dogId, Authentication auth) {
        User currentUser = userRepository.findByUsername(auth.getName()).orElseThrow();
        Dog dog = dogRepository.findById(dogId).orElseThrow(() -> new DogNotFoundException(dogId));

        // Only allow if admin or dog owner
        if (!currentUser.getRole().equals("ROLE_ADMIN") && !dog.getOwner().getId().equals(currentUser.getId())) {
            throw new IllegalArgumentException("You cannot view trainings for a dog you do not own.");
        }

        return trainingRepository.findByDogId(dogId)
                .stream()
                .map(DogTrainingResponseDTO::new)
                .toList();
    }

    // Create new training for a specific dog
    public DogTrainingResponseDTO createTrainingForDog(Integer dogId, DogTrainingRequestDTO dto, Authentication auth) {
        User currentUser = userRepository.findByUsername(auth.getName()).orElseThrow();
        Dog dog = dogRepository.findById(dogId).orElseThrow(() -> new DogNotFoundException(dogId));

        if (!currentUser.getRole().equals("ROLE_ADMIN") && !dog.getOwner().getId().equals(currentUser.getId())) {
            throw new IllegalArgumentException("You cannot add training for a dog you do not own.");
        }

        DogTraining training = new DogTraining();
        training.setActivity(dto.activity());
        training.setLocation(dto.location());
        training.setTrainingDate(dto.trainingDate());
        training.setDurationMinutes(dto.durationMinutes());
        training.setNotes(dto.notes());
        training.setDog(dog);

        return new DogTrainingResponseDTO(trainingRepository.save(training));
    }
}

