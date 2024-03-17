package org.vlasevsky.gym.service.map;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.vlasevsky.gym.model.Trainee;
import org.vlasevsky.gym.model.Trainer;
import org.vlasevsky.gym.model.Training;
import org.vlasevsky.gym.service.TraineeService;
import org.vlasevsky.gym.service.TrainerService;
import org.vlasevsky.gym.service.TrainingService;

@Slf4j
@Service
public class GymFacade {

    private final TraineeService traineeService;
    private final TrainerService trainerService;
    private final TrainingService trainingService;

    @Autowired
    public GymFacade(TraineeService traineeService, TrainerService trainerService, TrainingService trainingService) {
        this.traineeService = traineeService;
        this.trainerService = trainerService;
        this.trainingService = trainingService;
    }

    public void createTrainingSession(Long traineeId, Long trainerId, Training training) {
        log.info("Creating training session for traineeId: {} and trainerId: {}", traineeId, trainerId);
        Trainee trainee = traineeService.findById(traineeId);
        Trainer trainer = trainerService.findById(trainerId);
        training.setTrainee(trainee.getId());
        training.setTrainer(trainer.getId());
        trainingService.save(training);
    }
}