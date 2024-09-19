package com.vlasevsky.gym.integration.service;

import com.vlasevsky.gym.config.JmsConstants;
import com.vlasevsky.gym.dto.*;
import com.vlasevsky.gym.exceptions.TrainerNotFoundException;
import com.vlasevsky.gym.integration.IntegrationTestBase;
import com.vlasevsky.gym.integration.annotation.IT;
import com.vlasevsky.gym.model.TrainingType;
import com.vlasevsky.gym.service.map.TrainerServiceMap;
import jakarta.jms.JMSException;
import jakarta.jms.Message;
import jakarta.jms.TextMessage;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import static com.vlasevsky.gym.model.TrainingType.Type.CARDIO;
import static com.vlasevsky.gym.model.TrainingType.Type.STRENGTH_TRAINING;
import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.*;

@IT
public class TrainerServiceIntegrationTest extends IntegrationTestBase {

    @Autowired
    private TrainerServiceMap trainerServiceMap;

    @Autowired
    private JmsTemplate jmsTemplate;

    @Test
    public void testUpdateTrainerWorkload_trainerNotFound() {
        // Arrange
        TrainerWorkloadRequest request = new TrainerWorkloadRequest(
                "non.existent",
                "Non",
                "Existent",
                true,
                LocalDate.now(),
                60,
                TrainerWorkloadRequest.ActionType.ADD
        );

        // Act & Assert
        assertThrows(TrainerNotFoundException.class, () -> {
            trainerServiceMap.updateTrainerWorkload(request);
        });
    }

    @Test
    public void testFindTrainerByUsername_success() {
        // Arrange
        String username = "alice.johnson";

        // Act
        TrainerProfileReadDto trainerProfile = trainerServiceMap.findTrainerByUsername(username);

        // Assert
        assertNotNull(trainerProfile);
        assertEquals("alice.johnson", trainerProfile.username());
        assertEquals("Alice", trainerProfile.firstName());
        assertEquals("Johnson", trainerProfile.lastName());
        assertTrue(trainerProfile.specialization().contains(CARDIO));
    }

    @Test
    public void testUpdateTrainer_success() {
        // Arrange
        String username = "alice.johnson";
        TrainerCreateDto trainerCreateDto = new TrainerCreateDto(
                "UpdatedFirstName",
                "UpdatedLastName",
                List.of(CARDIO, STRENGTH_TRAINING),
        true
        );

        // Act
        TrainerProfileReadDto updatedTrainer = trainerServiceMap.update(username, trainerCreateDto);

        // Assert
        assertNotNull(updatedTrainer);
        assertEquals("UpdatedFirstName", updatedTrainer.firstName());
        assertEquals("UpdatedLastName", updatedTrainer.lastName());
        assertTrue(updatedTrainer.specialization().contains(CARDIO));
        assertTrue(updatedTrainer.specialization().contains(TrainingType.Type.STRENGTH_TRAINING));
    }
    @Test
    public void testFindAllTrainers_success() {
        // Act
        Set<TrainerReadDto> trainers = trainerServiceMap.findAll();

        // Assert
        assertNotNull(trainers);
        assertTrue(trainers.size() > 0); // Убедитесь, что у вас есть тренеры в базе данных
        assertEquals("alice.johnson", trainers.stream().map(TrainerReadDto::username).filter(username -> Objects.equals(username, "alice.johnson")).findFirst().get()); // Проверяем одного из тренеров
    }


    @Test
    public void testGetTrainersNotAssignedToTrainee_success() {
        // Arrange
        String traineeUsername = "john.doe";

        // Act
        Set<TrainerReadDto> trainers = trainerServiceMap.getTrainersNotAssignedToTrainee(traineeUsername);

        // Assert
        assertNotNull(trainers);
        assertTrue(trainers.isEmpty());
    }
}
