package com.vlasevsky.gym.controller;

import com.vlasevsky.gym.model.TrainingType;
import com.vlasevsky.gym.service.TrainingTypeService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

@WebMvcTest(TrainingTypeController.class)
class TrainingTypeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TrainingTypeService trainingTypeService;

    @Test
    void getTrainerProfile() throws Exception {
        // Given
        TrainingType cardioType = new TrainingType();
        cardioType.setId(1L);
        cardioType.setName(TrainingType.Type.CARDIO);

        TrainingType strengthType = new TrainingType();
        strengthType.setId(2L);
        strengthType.setName(TrainingType.Type.STRENGTH_TRAINING);

        Mockito.when(trainingTypeService.findAll()).thenReturn(List.of(cardioType, strengthType));

        // When
        ResultActions resultActions = mockMvc.perform(get("/training_types"));

        // Then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name", is("CARDIO")))
                .andExpect(jsonPath("$[1].name", is("STRENGTH_TRAINING")));
    }
}