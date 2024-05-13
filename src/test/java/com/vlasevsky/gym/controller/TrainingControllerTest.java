package com.vlasevsky.gym.controller;

import com.vlasevsky.gym.dto.TrainingCreateDto;
import com.vlasevsky.gym.service.TrainingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TrainingController.class)
class TrainingControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TrainingService trainingService;

    private TrainingCreateDto createDto;

    @BeforeEach
    void setUp() {
        createDto = new TrainingCreateDto("traineeUsername", "trainerUsername", "Test Training", null, 60);
    }

    @Test
    void registerTrainee() throws Exception {

        Mockito.doNothing().when(trainingService).create(createDto);

        ResultActions resultActions = mockMvc.perform(post("/trainings")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"traineeUsername\":\"traineeUsername\",\"trainerUsername\":\"trainerUsername\",\"name\":\"Test Training\",\"duration\":60}"));

        resultActions.andExpect(status().isOk());
    }
}