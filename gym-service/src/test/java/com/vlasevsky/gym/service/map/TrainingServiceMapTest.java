package com.vlasevsky.gym.service.map;

import com.vlasevsky.gym.dto.CredentialsDto;
import com.vlasevsky.gym.dto.TrainingCreateDto;
import com.vlasevsky.gym.exceptions.TraineeNotFoundException;
import com.vlasevsky.gym.exceptions.TrainerNotFoundException;
import com.vlasevsky.gym.exceptions.TrainingTypeNotFoundException;
import com.vlasevsky.gym.mapstruct.TrainingMapper;
import com.vlasevsky.gym.model.Trainee;
import com.vlasevsky.gym.model.Trainer;
import com.vlasevsky.gym.model.Training;
import com.vlasevsky.gym.model.TrainingType;
import com.vlasevsky.gym.repository.TraineeRepository;
import com.vlasevsky.gym.repository.TrainerRepository;
import com.vlasevsky.gym.repository.TrainingRepository;
import com.vlasevsky.gym.repository.TrainingTypeRepository;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TrainingServiceMapTest {

    @Mock
    private TrainingRepository trainingRepository;
    @Mock
    private TrainingMapper trainingMapper;

    @Mock
    private TraineeRepository traineeRepository;

    @Mock
    private TrainerRepository trainerRepository;

    @Mock
    private TrainingTypeRepository trainingTypeRepository;

    @InjectMocks
    private TrainingServiceMap trainingService;

    private static final String TRAINEE_USERNAME = "trainee1";
    private static final String TRAINER_USERNAME = "trainer1";
    private static final TrainingType.Type TRAINING_TYPE = TrainingType.Type.STRENGTH_TRAINING;
    private static final Trainee TRAINEE = new Trainee();
    private static final Trainer TRAINER = new Trainer();
    private static final Training TRAINING = new Training();
    private static final TrainingCreateDto TRAINING_CREATE_DTO = new TrainingCreateDto(TRAINEE_USERNAME, TRAINER_USERNAME, "Strength Training", TRAINING_TYPE, 60);
    private static final CredentialsDto CREDENTIALS_DTO = new CredentialsDto(TRAINER_USERNAME, "password");

    static {
        TRAINEE.setUsername(TRAINEE_USERNAME);
        TRAINER.setUsername(TRAINER_USERNAME);
        TRAINING.setId(1L);
    }

    @Test
    @SneakyThrows
    void testCreateTraining() {
        when(traineeRepository.findByUsername(anyString())).thenReturn(Optional.of(TRAINEE));
        when(trainerRepository.findByUsername(anyString())).thenReturn(Optional.of(TRAINER));
        when(trainingTypeRepository.findByName(any(TrainingType.Type.class))).thenReturn(Optional.of(new TrainingType()));
        when(trainingMapper.toEntity(any(TrainingCreateDto.class))).thenReturn(TRAINING);

        trainingService.create(TRAINING_CREATE_DTO);

        verify(trainingRepository, times(1)).save(any(Training.class));
    }

    @Test
    @SneakyThrows
    void testCreateTrainingTraineeNotFound() {
        when(traineeRepository.findByUsername(anyString())).thenReturn(Optional.empty());

        assertThrows(TraineeNotFoundException.class, () -> trainingService.create(TRAINING_CREATE_DTO));
    }

    @Test
    @SneakyThrows
    void testCreateTrainingTrainerNotFound() {
        when(traineeRepository.findByUsername(anyString())).thenReturn(Optional.of(TRAINEE));
        when(trainerRepository.findByUsername(anyString())).thenReturn(Optional.empty());

        assertThrows(TrainerNotFoundException.class, () -> trainingService.create(TRAINING_CREATE_DTO));
    }

    @Test
    @SneakyThrows
    void testCreateTrainingTypeNotFound() {
        when(traineeRepository.findByUsername(anyString())).thenReturn(Optional.of(TRAINEE));
        when(trainerRepository.findByUsername(anyString())).thenReturn(Optional.of(TRAINER));
        when(trainingTypeRepository.findByName(any(TrainingType.Type.class))).thenReturn(Optional.empty());

        assertThrows(TrainingTypeNotFoundException.class, () -> trainingService.create(TRAINING_CREATE_DTO));
    }

    @Test
    @SneakyThrows
    void testDeleteTraining() {
        when(trainingRepository.findById(anyLong())).thenReturn(Optional.of(TRAINING));

        boolean result = trainingService.delete(TRAINING.getId(), CREDENTIALS_DTO);

        assertTrue(result);
        verify(trainingRepository, times(1)).delete(any(Training.class));
    }

    @Test
    @SneakyThrows
    void testDeleteTrainingNotFound() {
        when(trainingRepository.findById(anyLong())).thenReturn(Optional.empty());

        boolean result = trainingService.delete(TRAINING.getId(), CREDENTIALS_DTO);

        assertFalse(result);
        verify(trainingRepository, never()).delete(any(Training.class));
    }
}
