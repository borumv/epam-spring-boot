package com.vlasevsky.gym.controller;

import com.vlasevsky.gym.dto.*;
import com.vlasevsky.gym.service.TraineeService;
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
import java.util.Date;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;
import static org.springframework.web.servlet.function.RequestPredicates.contentType;


@WebMvcTest(TraineeController.class)
class TraineeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TraineeService traineeService;

    private TraineeRegistrationDto registrationDto;
    private TraineeProfileReadDto profileReadDto;
    private CredentialsDto credentialsDto;
    private TraineeCreateAndUpdateDto updateDto;

    @BeforeEach
    void setUp() {
        registrationDto = new TraineeRegistrationDto("John", "Doe", null, null);
        profileReadDto = new TraineeProfileReadDto("John", "Doe", LocalDate.now(), "123 Street", true, List.of());
        credentialsDto = new CredentialsDto("john.doe", "password");
        updateDto = new TraineeCreateAndUpdateDto("john.doe", "John", "Doe", "123 Street", new Date(), true);
    }

    @Test
    void registerTrainee() throws Exception {
        Mockito.when(traineeService.register(registrationDto)).thenReturn(credentialsDto);

        ResultActions resultActions = mockMvc.perform(post("/trainees/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"firstName\":\"John\",\"lastName\":\"Doe\"}"));

        resultActions
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username").value("john.doe"))
                .andExpect(jsonPath("$.password").value("password"));
    }

    @Test
    void getTraineeProfile() throws Exception {

        Mockito.when(traineeService.findTraineeByUsername("john.doe")).thenReturn(profileReadDto);

        ResultActions resultActions = mockMvc.perform(get("/trainees/john.doe"));

        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"))
                .andExpect(jsonPath("$.address").value("123 Street"))
                .andExpect(jsonPath("$.isActive").value(true));
    }

    @Test
    void deleteTraineeProfile() throws Exception {

        ResultActions resultActions = mockMvc.perform(delete("/trainees/john.doe"));

        resultActions.andExpect(status().isOk());
        Mockito.verify(traineeService, times(1)).delete("john.doe");
    }

    @Test
    void changeTraineeActiveStatus() throws Exception {
        ResultActions resultActions = mockMvc.perform(patch("/trainees/john.doe")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"isActive\":false}"));

        resultActions.andExpect(status().isOk());
        Mockito.verify(traineeService, times(1)).changeActiveStatus("john.doe", new StatusUpdateDto(false));
    }

    @Test
    void updateTraineeTrainersList() throws Exception {

        List<String> trainers = List.of("trainer1", "trainer2");
        Mockito.when(traineeService.updateTraineeTrainers("john.doe", trainers)).thenReturn(profileReadDto);


        ResultActions resultActions = mockMvc.perform(put("/trainees/john.doe/trainers")
                .contentType(MediaType.APPLICATION_JSON)
                .param("trainers", "trainer1", "trainer2"));


        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"))
                .andExpect(jsonPath("$.address").value("123 Street"))
                .andExpect(jsonPath("$.isActive").value(true));
    }

    @Test
    void getTraineeTrainings() throws Exception {

        TrainingReadDto trainingReadDto = new TrainingReadDto(1L, "Training", LocalDate.now(), 60, null, null);
        List<TrainingReadDto> trainings = List.of(trainingReadDto);
        Mockito.when(traineeService.getTraineeTrainings("john.doe", null, null, null)).thenReturn(trainings);


        ResultActions resultActions = mockMvc.perform(get("/trainees/john.doe/trainings"));

        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name").value("Training"))
                .andExpect(jsonPath("$[0].duration").value(60));
    }

    @Test
    void hello() throws Exception {
        ResultActions resultActions = mockMvc.perform(get("/trainees"));

        resultActions
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Hello, World!")));
    }
}