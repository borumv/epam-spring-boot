package com.vlasevsky.gym.repository;

import com.vlasevsky.gym.model.Trainee;
import com.vlasevsky.gym.model.Training;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface TraineeRepository extends JpaRepository<Trainee, Long> {
    Optional<Trainee>findByUsername(String username);

    @Query("SELECT t FROM Training t JOIN t.trainee trn JOIN t.trainer tr WHERE trn.username = :username " +
            "AND t.date BETWEEN :fromDate AND :toDate AND tr.firstName LIKE :trainerName AND t.trainingType = :trainingType")
    List<Training> findTraineeTrainingsByUsernameAndCriteria(@Param("username") String username,
                                                             @Param("fromDate") Date fromDate,
                                                             @Param("toDate") Date toDate,
                                                             @Param("trainerName") String trainerName,
                                                             @Param("trainingType") String trainingType);
}
