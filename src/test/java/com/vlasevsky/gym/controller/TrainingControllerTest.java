package com.vlasevsky.gym.controller;

import com.vlasevsky.gym.dto.TrainingCreateDto;
import com.vlasevsky.gym.service.TrainingService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class TrainingControllerTest {

    @Mock
    private TrainingService trainingService;

    @InjectMocks
    private TrainingController trainingController;

    @Test
    void testRegisterTrainee() {
        TrainingCreateDto createDto = new TrainingCreateDto("trainee1", "trainer1", "Strength Training", null, 60);

        ResponseEntity<Void> result = trainingController.registerTrainee(createDto);

        assertEquals(ResponseEntity.ok().build(), result);
        verify(trainingService).create(any(TrainingCreateDto.class));
    }
}
