package com.vlasevsky.gym.controller;

import com.vlasevsky.gym.dto.*;
import com.vlasevsky.gym.model.TrainingType;
import com.vlasevsky.gym.service.TrainerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDate;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(TrainerController.class)
class TrainerControllerTest {


    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private TrainerService trainerService;

    private TrainerRegistrationDto registrationDto;
    private TrainerProfileReadDto profileReadDto;
    private CredentialsDto credentialsDto;
    private TrainerCreateDto updateDto;

    @BeforeEach
    void setUp() {
        registrationDto = new TrainerRegistrationDto("John", "Doe", List.of(TrainingType.Type.CARDIO));
        profileReadDto = new TrainerProfileReadDto("john.doe", "John", "Doe", List.of(TrainingType.Type.CARDIO), List.of());
        credentialsDto = new CredentialsDto("john.doe", "password");
        updateDto = new TrainerCreateDto("John", "Doe", List.of(TrainingType.Type.CARDIO), true);
    }

    @Test
    void getTrainers() throws Exception {

        TrainerReadDto trainerReadDto = new TrainerReadDto(1L, "john.doe", "John", "Doe", List.of(TrainingType.Type.CARDIO));
        Mockito.when(trainerService.findAll()).thenReturn(List.of(trainerReadDto));

        ResultActions resultActions = mockMvc.perform(get("/trainers"));

        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].username").value("john.doe"));
    }

    @Test
    void registerTrainee() throws Exception {

        Mockito.when(trainerService.register(registrationDto)).thenReturn(credentialsDto);

        ResultActions resultActions = mockMvc.perform(post("/trainers/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"firstName\":\"John\",\"lastName\":\"Doe\",\"specializations\":[\"CARDIO\"]}"));

        resultActions
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username").value("john.doe"))
                .andExpect(jsonPath("$.password").value("password"));
    }

    @Test
    void getTrainerProfile() throws Exception {

        Mockito.when(trainerService.findTrainerByUsername("john.doe")).thenReturn(profileReadDto);

        ResultActions resultActions = mockMvc.perform(get("/trainers/john.doe"));

        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"))
                .andExpect(jsonPath("$.specialization[0]").value("CARDIO"));
    }

    @Test
    void changeTrainerActiveStatus() throws Exception {

        ResultActions resultActions = mockMvc.perform(patch("/trainers/john.doe")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"isActive\":false}"));

        resultActions.andExpect(status().isOk());
        Mockito.verify(trainerService, times(1)).changeActiveStatus("john.doe", new StatusUpdateDto(false));
    }

    @Test
    void getTrainerTrainings() throws Exception {

        TrainingReadDto trainingReadDto = new TrainingReadDto(1L, "Training", LocalDate.now(), 60, null, null);
        List<TrainingReadDto> trainings = List.of(trainingReadDto);
        Mockito.when(trainerService.getTrainerTrainings("john.doe", null, null, null)).thenReturn(trainings);

        ResultActions resultActions = mockMvc.perform(get("/trainers/john.doe/trainings"));

        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name").value("Training"))
                .andExpect(jsonPath("$[0].duration").value(60));
    }

    @Test
    void getTrainersNotAssignedToTrainee() throws Exception {

        TrainerReadDto trainerReadDto = new TrainerReadDto(1L, "john.doe", "John", "Doe", List.of(TrainingType.Type.CARDIO));
        Mockito.when(trainerService.getTrainersNotAssignedToTrainee("traineeUsername")).thenReturn(List.of(trainerReadDto));

        ResultActions resultActions = mockMvc.perform(get("/trainers?unassignedTraineeUsername=traineeUsername"));

        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].username").value("john.doe"));
    }
}