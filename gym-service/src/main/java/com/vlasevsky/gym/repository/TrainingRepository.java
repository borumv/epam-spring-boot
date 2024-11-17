package com.vlasevsky.gym.repository;

import com.vlasevsky.gym.model.Trainee;
import com.vlasevsky.gym.model.Training;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Set;

@Repository
public interface TrainingRepository {


    Set<Training> findTrainingsByTraineeAndPeriodAndTrainer(@Param("traineeUsername") String traineeUsername,
                                                            @Param("fromDate") LocalDateTime fromDate,
                                                            @Param("toDate") LocalDateTime toDate,
                                                            @Param("trainerName") String trainerName);


    Set<Training> findTrainingsByTrainerAndPeriodAndTrainee(@Param("trainerUsername") String trainerUsername,
                                                             @Param("fromDate") LocalDateTime fromDate,
                                                             @Param("toDate") LocalDateTime toDate,
                                                             @Param("traineeName") String traineeName);

    void deleteAllByTrainee(Trainee trainee);

    void save(Training training);
}
