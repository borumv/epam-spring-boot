package com.vlasevsky.gym.repository;

import com.vlasevsky.gym.model.Trainee;
import com.vlasevsky.gym.model.Training;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface TraineeRepository {
    //@EntityGraph(attributePaths = {"trainers", "trainings"})
    Optional<Trainee> findByUsername(String username);

    //@EntityGraph(attributePaths = {"trainee", "trainer", "trainingType"})

    List<Training> findTraineeTrainingsByUsernameAndCriteria(@Param("username") String username,
                                                             @Param("fromDate") Date fromDate,
                                                             @Param("toDate") Date toDate,
                                                             @Param("trainerName") String trainerName,
                                                             @Param("trainingType") String trainingType);

    void save(Trainee trainee);

    void deleteById(Long id);
}
