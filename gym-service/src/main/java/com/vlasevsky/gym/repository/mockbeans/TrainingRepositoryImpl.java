package com.vlasevsky.gym.repository.mockbeans;

import com.vlasevsky.gym.model.Trainee;
import com.vlasevsky.gym.model.Training;
import com.vlasevsky.gym.repository.TrainingRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Set;
@Component
public class TrainingRepositoryImpl implements TrainingRepository {
    @Override
    public Set<Training> findTrainingsByTraineeAndPeriodAndTrainer(String traineeUsername, LocalDateTime fromDate, LocalDateTime toDate, String trainerName) {
        return null;
    }

    @Override
    public Set<Training> findTrainingsByTrainerAndPeriodAndTrainee(String trainerUsername, LocalDateTime fromDate, LocalDateTime toDate, String traineeName) {
        return null;
    }

    @Override
    public void deleteAllByTrainee(Trainee trainee) {

    }

    @Override
    public void save(Training training) {

    }
}
