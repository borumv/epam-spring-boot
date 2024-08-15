package com.vlasevsky.gym.service.map;

import com.vlasevsky.gym.dto.*;
import com.vlasevsky.gym.exceptions.TrainerNotFoundException;
import com.vlasevsky.gym.mapstruct.TraineeMapper;
import com.vlasevsky.gym.mapstruct.TrainerMapper;
import com.vlasevsky.gym.mapstruct.TrainingMapper;
import com.vlasevsky.gym.model.Trainer;
import com.vlasevsky.gym.model.Training;
import com.vlasevsky.gym.model.TrainingType;
import com.vlasevsky.gym.repository.TrainerRepository;
import com.vlasevsky.gym.repository.TrainingRepository;
import com.vlasevsky.gym.repository.TrainingTypeRepository;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TrainerServiceMapTest {

    @Mock
    private TrainerRepository trainerRepository;

    @Mock
    private TrainingRepository trainingRepository;

    @Mock
    private TrainingTypeRepository trainingTypeRepository;

    @Mock
    private TrainerMapper trainerMapper;

    @Mock
    private TrainingMapper trainingMapper;

    @InjectMocks
    private TrainerServiceMap trainerService;

    private static final String TRAINER_USERNAME = "trainer1";
    private static final Trainer TRAINER = new Trainer();
    private static final TrainerProfileReadDto TRAINER_PROFILE_DTO = new TrainerProfileReadDto(TRAINER_USERNAME, "Trainer", "One", Collections.emptyList(), Collections.emptyList());
    private static final TrainerCreateDto TRAINER_CREATE_DTO = new TrainerCreateDto("Trainer", "One", Collections.emptyList(), true);
    private static final StatusUpdateDto STATUS_UPDATE_DTO = new StatusUpdateDto(true);
    private static final List<TrainingReadDto> TRAINING_DTO_LIST = Collections.emptyList();
    private static final List<Training> TRAININGS = Collections.emptyList();
    private static final List<TrainerReadDto> TRAINER_DTO_LIST = Collections.emptyList();

    static {
        TRAINER.setUsername(TRAINER_USERNAME);
    }

    @Test
    @SneakyThrows
    void testFindAll() {
        when(trainerRepository.findAll()).thenReturn(Collections.singletonList(TRAINER));
        when(trainerMapper.toDTOList(anyList())).thenReturn(TRAINER_DTO_LIST);

        List<TrainerReadDto> trainers = trainerService.findAll();

        assertEquals(TRAINER_DTO_LIST, trainers);
    }

    @Test
    @SneakyThrows
    void testChangeActiveStatus() {
        when(trainerRepository.findByUsername(anyString())).thenReturn(Optional.of(TRAINER));

        trainerService.changeActiveStatus(TRAINER_USERNAME, STATUS_UPDATE_DTO);

        verify(trainerRepository, times(1)).save(any(Trainer.class));
    }

    @Test
    @SneakyThrows
    void testGetTrainerTrainings() {
        when(trainingRepository.findTrainingsByTrainerAndPeriodAndTrainee(anyString(), any(LocalDateTime.class), any(LocalDateTime.class), anyString()))
                .thenReturn(TRAININGS);
        when(trainingMapper.toDTOList(anyList())).thenReturn(TRAINING_DTO_LIST);

        List<TrainingReadDto> trainings = trainerService.getTrainerTrainings(TRAINER_USERNAME, LocalDateTime.now().minusDays(1), LocalDateTime.now(), "trainee1");

        assertEquals(TRAINING_DTO_LIST, trainings);
    }

    @Test
    @SneakyThrows
    void testGetTrainersNotAssignedToTrainee() {
        when(trainerRepository.findTrainersNotAssignedToTrainee(anyString())).thenReturn(Collections.singletonList(TRAINER));
        when(trainerMapper.toDTOList(anyList())).thenReturn(TRAINER_DTO_LIST);

        List<TrainerReadDto> trainers = trainerService.getTrainersNotAssignedToTrainee("trainee1");

        assertEquals(TRAINER_DTO_LIST, trainers);
    }
}
