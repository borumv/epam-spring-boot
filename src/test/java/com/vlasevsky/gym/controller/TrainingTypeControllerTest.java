package com.vlasevsky.gym.controller;

import com.vlasevsky.gym.model.TrainingType;
import com.vlasevsky.gym.service.TrainingTypeService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TrainingTypeControllerTest {

    @Mock
    private TrainingTypeService trainingTypeService;

    @InjectMocks
    private TrainingTypeController trainingTypeController;

    @Test
    void testGetTrainerProfile() {
        List<TrainingType> trainingTypes = Collections.singletonList(new TrainingType());

        when(trainingTypeService.findAll()).thenReturn(trainingTypes);

        ResponseEntity<List<TrainingType>> result = trainingTypeController.getTrainerProfile();

        assertEquals(ResponseEntity.ok(trainingTypes), result);
    }
}
