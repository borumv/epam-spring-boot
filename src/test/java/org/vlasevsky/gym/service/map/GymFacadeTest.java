package org.vlasevsky.gym.service.map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.vlasevsky.gym.model.Trainee;
import org.vlasevsky.gym.model.Trainer;
import org.vlasevsky.gym.model.Training;
import org.vlasevsky.gym.service.TraineeService;
import org.vlasevsky.gym.service.TrainerService;
import org.vlasevsky.gym.service.TrainingService;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


class GymFacadeTest {

    @Mock
    private TraineeService traineeService;

    @Mock
    private TrainerService trainerService;

    @Mock
    private TrainingService trainingService;

    @InjectMocks
    private GymFacade gymFacade;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateTrainingSession() {
        Long traineeId = 1L;
        Long trainerId = 2L;
        Training training = new Training();

        Trainee mockTrainee = new Trainee();
        mockTrainee.setId(traineeId);

        Trainer mockTrainer = new Trainer();
        mockTrainer.setId(trainerId);

        when(traineeService.findById(traineeId)).thenReturn(mockTrainee);
        when(trainerService.findById(trainerId)).thenReturn(mockTrainer);

        gymFacade.createTrainingSession(traineeId, trainerId, training);

        assertEquals(traineeId, training.getTrainee());
        assertEquals(trainerId, training.getTrainer());
        verify(trainingService).save(training);
    }
}