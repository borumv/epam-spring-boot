package com.vlasevsky.gym.integration.service;

import com.vlasevsky.gym.dto.CredentialsDto;
import com.vlasevsky.gym.dto.TrainingCreateDto;
import com.vlasevsky.gym.exceptions.TraineeNotFoundException;
import com.vlasevsky.gym.exceptions.TrainerNotFoundException;
import com.vlasevsky.gym.integration.IntegrationTestBase;
import com.vlasevsky.gym.integration.annotation.IT;
import com.vlasevsky.gym.model.Training;
import com.vlasevsky.gym.model.TrainingType;
import com.vlasevsky.gym.repository.TrainingRepository;
import com.vlasevsky.gym.service.map.TrainingServiceMap;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@IT
public class TrainingServiceIntegrationTest extends IntegrationTestBase {

    @Autowired
    private TrainingServiceMap trainingService;

    @Autowired
    private TrainingRepository trainingRepository;

    @Test
    public void testCreateTraining_success() {
        // Arrange
        TrainingCreateDto createDto = new TrainingCreateDto(
                "john.doe",
                "alice.johnson",
                "Cardio-training",
                TrainingType.Type.CARDIO,
                33
        );

        // Act
        trainingService.create(createDto);

        // Assert
        Optional<Training> training = trainingRepository.findAll().stream().findFirst();
        assertTrue(training.isPresent());
        assertEquals("john.doe", training.get().getTrainee().getUsername());
        assertEquals("alice.johnson", training.get().getTrainer().getUsername());
        assertEquals(TrainingType.Type.CARDIO, training.get().getTrainingType().getName());
    }

    @Test
    public void testCreateTraining_traineeNotFound() {
        // Arrange
        TrainingCreateDto createDto = new TrainingCreateDto(
                "john.dode",
                "alice.johnson",
                "Cardio-training",
                TrainingType.Type.CARDIO,
                33
        );

        // Act & Assert
        assertThrows(TraineeNotFoundException.class, () -> {
            trainingService.create(createDto);
        });
    }

    @Test
    public void testCreateTraining_trainerNotFound() {
        // Arrange
        TrainingCreateDto createDto = new TrainingCreateDto(
                "john.doe",
                "alice.johnason",
                "Cardio-training",
                TrainingType.Type.CARDIO,
                33
        );

        // Act & Assert
        assertThrows(TrainerNotFoundException.class, () -> {
            trainingService.create(createDto);
        });
    }

}
