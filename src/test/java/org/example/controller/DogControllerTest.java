package org.example.controller;

import org.example.dto.DogResponseDTO;
import org.example.dto.DogTrainingResponseDTO;
import org.example.service.DogService;
import org.example.service.DogTrainingService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(DogController.class)
class DogControllerRoleBasedTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private DogService dogService;

    @MockitoBean
    private DogTrainingService dogTrainingService;

    private DogResponseDTO createMockDog(Integer id, String name, String breed, Integer ownerId) {
        // Create a mock Dog entity to use DogResponseDTO constructor
        org.example.entities.Dog mockDog = new org.example.entities.Dog();
        mockDog.setId(id);
        mockDog.setName(name);
        mockDog.setBreed(breed);
        mockDog.setBirthdate(LocalDate.of(2020, 1, 1));

        org.example.entities.User mockUser = new org.example.entities.User();
        mockUser.setId(ownerId);
        mockUser.setUsername("user" + ownerId);
        mockDog.setOwner(mockUser);

        return new DogResponseDTO(mockDog);
    }

    private DogTrainingResponseDTO createMockTraining(Integer id, String activity, Integer dogId) {
        // Create a mock Dogtraining entity
        org.example.entities.DogTraining mockTraining = new org.example.entities.DogTraining();
        mockTraining.setId(id);
        mockTraining.setActivity(activity);
        mockTraining.setLocation("Park");
        mockTraining.setTrainingDate(LocalDate.now());
        mockTraining.setDurationMinutes(30);
        mockTraining.setNotes("Good training");
        mockTraining.setCreatedAt(LocalDateTime.now());

        org.example.entities.Dog mockDog = new org.example.entities.Dog();
        mockDog.setId(dogId);
        mockTraining.setDog(mockDog);

        return new DogTrainingResponseDTO(mockTraining);
    }

    // TEST GET api/dogs

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void getDogs_shouldReturn4DogsForAdmin() throws Exception {
        // Arrange - Admin sees all 4 dogs
        List<DogResponseDTO> allDogs = Arrays.asList(
                createMockDog(1, "Buddy", "Golden Retriever", 1),
                createMockDog(2, "Max", "Labrador", 1),
                createMockDog(3, "Bella", "Poodle", 2),
                createMockDog(4, "Charlie", "Bulldog", 2)
        );

        when(dogService.getAllDogs(any())).thenReturn(allDogs);

        // Act & Assert
        mockMvc.perform(get("/api/dogs")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(4))
                .andExpect(jsonPath("$[0].name").value("Buddy"))
                .andExpect(jsonPath("$[1].name").value("Max"))
                .andExpect(jsonPath("$[2].name").value("Bella"))
                .andExpect(jsonPath("$[3].name").value("Charlie"));
    }

    @Test
    @WithMockUser(username = "user")
    void getDogs_shouldReturn2DogsForRegularUser() throws Exception {
        // Arrange - User1 sees there 2 dogs
        List<DogResponseDTO> userDogs = Arrays.asList(
                createMockDog(1, "Buddy", "Golden Retriever", 1),
                createMockDog(2, "Max", "Labrador", 1)
        );

        when(dogService.getAllDogs(any())).thenReturn(userDogs);

        // Act & Assert
        mockMvc.perform(get("/api/dogs")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("Buddy"))
                .andExpect(jsonPath("$[1].name").value("Max"));
    }

    @Test
    @WithMockUser(username = "user2")
    void getDogs_shouldReturnDifferent2DogsForAnotherUser() throws Exception {
        // Arrange - User2 sees there 2 dogs
        List<DogResponseDTO> userDogs = Arrays.asList(
                createMockDog(3, "Bella", "Poodle", 2),
                createMockDog(4, "Charlie", "Bulldog", 2)
        );

        when(dogService.getAllDogs(any())).thenReturn(userDogs);

        // Act & Assert
        mockMvc.perform(get("/api/dogs")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("Bella"))
                .andExpect(jsonPath("$[1].name").value("Charlie"));
    }

    @Test
    void getDogs_shouldReturnUnauthorizedForUnauthenticatedUser() throws Exception {
        mockMvc.perform(get("/api/dogs"))
                .andExpect(status().isUnauthorized());
    }

    // TEST GET api/dogs/{id}
}