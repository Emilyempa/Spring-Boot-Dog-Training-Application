package org.example.service;

import org.example.dto.DogRequestDTO;
import org.example.dto.DogResponseDTO;
import org.example.dto.DogTrainingRequestDTO;
import org.example.dto.DogTrainingResponseDTO;
import org.example.entities.Dog;
import org.example.entities.User;
import org.example.errorhandling.DogNotFoundException;
import org.example.repository.DogRepository;
import org.example.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DogService {

    private final DogRepository dogRepository;
    private final UserRepository userRepository;
    private final DogTrainingService dogTrainingService;

    public DogService(DogRepository dogRepository, UserRepository userRepository, DogTrainingService dogTrainingService) {
        this.dogRepository = dogRepository;
        this.userRepository = userRepository;
        this.dogTrainingService = dogTrainingService;
    }


    public List<DogResponseDTO> getAllDogs(Authentication auth) {
        User currentUser = userRepository.findByUsername(auth.getName())
                .orElseThrow();

        if (currentUser.getRole().equals("ROLE_ADMIN")) {
            return dogRepository.findAll()
                    .stream().map(DogResponseDTO::new).toList();
        }

        return dogRepository.findByOwnerId(currentUser.getId())
                .stream().map(DogResponseDTO::new).toList();
    }

    public DogResponseDTO getDogById(Integer id, Authentication auth) {
        User currentUser = userRepository.findByUsername(auth.getName()).orElseThrow();

        Dog dog;
        if (currentUser.getRole().equals("ROLE_ADMIN")) {
            dog = dogRepository.findById(id).orElseThrow(() -> new DogNotFoundException(id));
        } else {
            dog = dogRepository.findByIdAndOwnerId(id, currentUser.getId())
                    .orElseThrow(() -> new DogNotFoundException(id));
        }
        return new DogResponseDTO(dog);
    }

    public DogResponseDTO createDog(DogRequestDTO dto, Authentication auth) {
        User currentUser = userRepository.findByUsername(auth.getName())
                .orElseThrow();

        Dog dog = new Dog();
        dog.setName(dto.name());
        dog.setBreed(dto.breed());
        dog.setBirthdate(dto.birthdate());
        dog.setOwner(currentUser);

        return new DogResponseDTO(dogRepository.save(dog));
    }

    public DogResponseDTO updateDog(Integer id, DogRequestDTO dto, Authentication auth) {
        User currentUser = userRepository.findByUsername(auth.getName())
                .orElseThrow();

        Dog dog = (currentUser.getRole().equals("ROLE_ADMIN"))
                ? dogRepository.findById(id).orElseThrow(() -> new DogNotFoundException(id))
                : dogRepository.findByIdAndOwnerId(id, currentUser.getId())
                .orElseThrow(() -> new DogNotFoundException(id));

        dog.setName(dto.name());
        dog.setBreed(dto.breed());
        dog.setBirthdate(dto.birthdate());

        return new DogResponseDTO(dogRepository.save(dog));
    }

    public void deleteDog(Integer id, Authentication auth) {
        User currentUser = userRepository.findByUsername(auth.getName()).orElseThrow();

        Dog dog = (currentUser.getRole().equals("ROLE_ADMIN"))
                ? dogRepository.findById(id).orElseThrow(() -> new DogNotFoundException(id))
                : dogRepository.findByIdAndOwnerId(id, currentUser.getId())
                .orElseThrow(() -> new DogNotFoundException(id));

        dogRepository.delete(dog);
    }

    // Add new training for a specific dog
    public DogTrainingResponseDTO addTrainingForDog(Integer dogId, DogTrainingRequestDTO dto, Authentication auth) {
        return dogTrainingService.createTrainingForDog(dogId, dto, auth);
    }
}