package com.vlasevsky.gym.service.map;

import com.vlasevsky.gym.dto.StatusUpdateDto;
import com.vlasevsky.gym.dto.TraineeCreateAndUpdateDto;
import com.vlasevsky.gym.dto.TraineeProfileReadDto;
import com.vlasevsky.gym.dto.TrainingReadDto;
import com.vlasevsky.gym.exceptions.TraineeNotFoundException;
import com.vlasevsky.gym.exceptions.UserNotFoundException;
import com.vlasevsky.gym.mapstruct.TraineeMapper;
import com.vlasevsky.gym.mapstruct.TrainerMapper;
import com.vlasevsky.gym.mapstruct.TrainingMapper;
import com.vlasevsky.gym.model.Trainee;
import com.vlasevsky.gym.model.Trainer;
import com.vlasevsky.gym.model.Training;
import com.vlasevsky.gym.repository.TraineeRepository;
import com.vlasevsky.gym.repository.TrainerRepository;
import com.vlasevsky.gym.repository.TrainingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class TraineeServiceMapTest {

    @Mock
    private TraineeRepository traineeRepository;
    @Mock
    private TrainerRepository trainerRepository;
    @Mock
    private TrainingRepository trainingRepository;
    @Spy
    private TraineeMapper traineeMapper = Mappers.getMapper(TraineeMapper.class);

    @Spy
    private TrainerMapper trainerMapper = Mappers.getMapper(TrainerMapper.class);
    @Spy
    private TrainingMapper trainingMapper = Mappers.getMapper(TrainingMapper.class);
    @InjectMocks
    private TraineeServiceMap traineeServiceMap;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void deleteTraineeThatExists() {

        String username = "testUser";
        Trainee trainee = new Trainee();
        trainee.setUsername(username);
        trainee.setTrainers(new ArrayList<>());
        trainee.setTrainings(new ArrayList<>());
        when(traineeRepository.findByUsername(username)).thenReturn(Optional.of(trainee));


        traineeServiceMap.delete(username);

        verify(traineeRepository, times(1)).deleteById(trainee.getId());
        verify(traineeRepository, times(1)).findByUsername(username);
    }

    @Test
    void deleteTraineeThatDoesNotExist() {

        String username = "nonExistentUser";
        when(traineeRepository.findByUsername(username)).thenReturn(Optional.empty());

        assertThrows(TraineeNotFoundException.class, () -> traineeServiceMap.delete(username));
    }

    @Test
    void changeActiveStatusForExistingTrainee() {
        // Given
        String username = "testUser";
        Trainee trainee = new Trainee();
        trainee.setUsername(username);
        when(traineeRepository.findByUsername(username)).thenReturn(Optional.of(trainee));
        StatusUpdateDto statusUpdateDto = new StatusUpdateDto(false);

        // When
        traineeServiceMap.changeActiveStatus(username, statusUpdateDto);

        // Then
        assertEquals(false, trainee.getIsActive());
        verify(traineeRepository, times(1)).save(trainee);
    }

    @Test
    void changeActiveStatusForNonExistingTrainee() {
        // Given
        String username = "nonExistentUser";
        when(traineeRepository.findByUsername(username)).thenReturn(Optional.empty());
        StatusUpdateDto statusUpdateDto = new StatusUpdateDto(false);

        // When/Then
        assertThrows(TraineeNotFoundException.class, () -> traineeServiceMap.changeActiveStatus(username, statusUpdateDto));
    }

    @Test
    void getTraineeTrainingsForNonExistingTrainee() {
        // Given
        String traineeName = "nonExistentTrainee";
        String trainerUsername = "testTrainer";
        LocalDateTime from = LocalDateTime.now().minusDays(1);
        LocalDateTime to = LocalDateTime.now();
        when(trainingRepository.findTrainingsByTraineeAndPeriodAndTrainer(trainerUsername, from, to, traineeName))
                .thenReturn(List.of());

        // When
        List<TrainingReadDto> result = traineeServiceMap.getTraineeTrainings(trainerUsername, from, to, traineeName);

        // Then
        assertTrue(result.isEmpty());
        verify(trainingRepository, times(1)).findTrainingsByTraineeAndPeriodAndTrainer(trainerUsername, from, to, traineeName);
    }


    @Test
    void updateExistingTrainee() {
        // Given
        String username = "testUser";
        Trainee trainee = new Trainee();
        trainee.setUsername(username);
        trainee.setTrainers(new ArrayList<>());  // Убедимся, что trainers не null и изменяем
        when(traineeRepository.findByUsername(username)).thenReturn(Optional.of(trainee));
        TraineeCreateAndUpdateDto dto = new TraineeCreateAndUpdateDto(username, "John", "Doe", "123 Street", null, true);

        // When
        TraineeProfileReadDto result = traineeServiceMap.update(username, dto);

        // Then
        assertEquals("John", trainee.getFirstName());
        assertEquals("Doe", trainee.getLastName());
        assertEquals("123 Street", trainee.getAddress());
        verify(traineeRepository, times(1)).save(trainee);
    }

    @Test
    void updateNonExistingTrainee() {
        // Given
        String username = "nonExistentUser";
        when(traineeRepository.findByUsername(username)).thenReturn(Optional.empty());
        TraineeCreateAndUpdateDto dto = new TraineeCreateAndUpdateDto(username, "John", "Doe", "123 Street", null, true);

        // When/Then
        assertThrows(UserNotFoundException.class, () -> traineeServiceMap.update(username, dto));
    }

    @Test
    void findTraineeByUsernameThatExists() {
        // Given
        String username = "testUser";
        Trainee trainee = new Trainee();
        trainee.setUsername(username);
        trainee.setTrainers(new ArrayList<>());
        trainee.setTrainings(new ArrayList<>());
        when(traineeRepository.findByUsername(username)).thenReturn(Optional.of(trainee));

        // When
        traineeServiceMap.delete(username);

        // Then
        verify(traineeRepository, times(1)).deleteById(trainee.getId());
        verify(traineeRepository, times(1)).findByUsername(username);
    }

    @Test
    void findTraineeByUsernameThatDoesNotExist() {
        // Given
        String username = "nonExistentUser";
        when(traineeRepository.findByUsername(username)).thenReturn(Optional.empty());

        // When/Then
        assertThrows(UserNotFoundException.class, () -> traineeServiceMap.findTraineeByUsername(username));
    }

    @Test
    void updateTraineeTrainersForExistingTrainee() {
        // Given
        String username = "testUser";
        Trainee trainee = new Trainee();
        trainee.setUsername(username);
        trainee.setTrainers(new ArrayList<>());  // Убедимся, что trainers не null и изменяем
        List<Trainer> trainers = List.of(new Trainer());
        when(traineeRepository.findByUsername(username)).thenReturn(Optional.of(trainee));
        when(trainerRepository.findAllTrainersByUsername(List.of("trainer1", "trainer2"))).thenReturn(trainers);

        // When
        TraineeProfileReadDto result = traineeServiceMap.updateTraineeTrainers(username, List.of("trainer1", "trainer2"));

        // Then
        assertEquals(trainers, trainee.getTrainers());
        verify(traineeRepository, times(1)).save(trainee);
    }

    @Test
    void updateTraineeTrainersForNonExistingTrainee() {
        // Given
        String username = "nonExistentUser";
        when(traineeRepository.findByUsername(username)).thenReturn(Optional.empty());

        // When/Then
        assertThrows(TraineeNotFoundException.class, () -> traineeServiceMap.updateTraineeTrainers(username, List.of("trainer1", "trainer2")));
    }
}