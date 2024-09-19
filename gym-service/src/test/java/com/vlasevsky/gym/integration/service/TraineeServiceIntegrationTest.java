package com.vlasevsky.gym.integration.service;

import com.vlasevsky.gym.dto.StatusUpdateDto;
import com.vlasevsky.gym.dto.TraineeCreateAndUpdateDto;
import com.vlasevsky.gym.dto.TraineeProfileReadDto;
import com.vlasevsky.gym.dto.TrainingReadDto;
import com.vlasevsky.gym.exceptions.TraineeNotFoundException;
import com.vlasevsky.gym.exceptions.UserNotFoundException;
import com.vlasevsky.gym.integration.IntegrationTestBase;
import com.vlasevsky.gym.integration.annotation.IT;
import com.vlasevsky.gym.service.map.TraineeServiceMap;
import com.vlasevsky.gym.service.map.TrainerServiceMap;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.*;

@IT
public class TraineeServiceIntegrationTest extends IntegrationTestBase {

    @Autowired
    private TraineeServiceMap traineeServiceMap;

    @Test
    public void testDeleteTrainee_success() {
        // Arrange
        String username = "john.doe";

        // Act
        traineeServiceMap.delete(username);

        // Assert
        assertThrows(UserNotFoundException.class, () -> {
            traineeServiceMap.findTraineeByUsername(username);
        });
    }

    @Test
    public void testChangeActiveStatus_success() {
        // Arrange
        String username = "john.doe";
        StatusUpdateDto statusUpdateDto = new StatusUpdateDto(false);

        // Act
        traineeServiceMap.changeActiveStatus(username, statusUpdateDto);

        // Assert
        TraineeProfileReadDto trainee = traineeServiceMap.findTraineeByUsername(username);
        assertFalse(trainee.isActive());
    }

    @Test
    public void testUpdateTrainee_success() {
        // Arrange
        String username = "john.doe";
        Date dateOfBirth = Date.valueOf(LocalDate.of(1990, 1, 1));  // Преобразование LocalDate в Date

        TraineeCreateAndUpdateDto updateDto = new TraineeCreateAndUpdateDto(
                "john.doe",
                "John",
                "Doe",
                "456 New Street",
                dateOfBirth,  // Использование java.sql.Date вместо LocalDate
                true
        );

        // Act
        TraineeProfileReadDto updatedTrainee = traineeServiceMap.update(username, updateDto);

        // Assert
        assertNotNull(updatedTrainee);
        assertEquals("Doe", updatedTrainee.lastName());
        assertEquals("456 New Street", updatedTrainee.address());
    }

    @Test
    public void testFindTraineeByUsername_success() {
        // Arrange
        String username = "john.doe";

        // Act
        TraineeProfileReadDto traineeProfile = traineeServiceMap.findTraineeByUsername(username);

        // Assert
        Assertions.assertNotNull(traineeProfile);
        assertEquals("Doe", traineeProfile.lastName());
        assertEquals("John", traineeProfile.firstName());
    }
    @Test
    public void testUpdateTraineeTrainers_success() {
        // Arrange
        String username = "john.doe";
        List<String> trainerUsernames = List.of("alice.johnson", "bob.brown");

        // Act
        TraineeProfileReadDto updatedTrainee = traineeServiceMap.updateTraineeTrainers(username, trainerUsernames);

        // Assert
        assertNotNull(updatedTrainee);
        assertEquals(2, updatedTrainee.trainers().size());
        assertTrue(updatedTrainee.trainers().stream()
                .anyMatch(trainer -> trainer.username().equals("alice.johnson")));
    }

}
