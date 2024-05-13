package com.vlasevsky.gym.repository;

import com.vlasevsky.gym.model.Training;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TrainingRepository extends JpaRepository<Training, Long> {


    @Query("SELECT t FROM Training t WHERE t.trainee.username = :traineeUsername " +
            "AND (:fromDate IS NULL OR t.date >= :fromDate) " +
            "AND (:toDate IS NULL OR t.date <= :toDate) " +
            "AND (:trainerName IS NULL OR t.trainer.firstName LIKE :trainerName OR t.trainer.lastName LIKE :trainerName)")
    List<Training> findTrainingsByTraineeAndPeriodAndTrainer(@Param("traineeUsername") String traineeUsername,
                                                             @Param("fromDate") LocalDateTime fromDate,
                                                             @Param("toDate") LocalDateTime toDate,
                                                             @Param("trainerName") String trainerName);

    @Query("SELECT t FROM Training t WHERE t.trainer.username = :trainerUsername " +
            "AND (:fromDate IS NULL OR t.date >= :fromDate) " +
            "AND (:toDate IS NULL OR t.date <= :toDate) " +
            "AND (:traineeName IS NULL OR t.trainee.firstName LIKE :traineeName OR t.trainee.lastName LIKE :traineeName)")
    List<Training> findTrainingsByTrainerAndPeriodAndTrainee(@Param("trainerUsername") String trainerUsername,
                                                             @Param("fromDate") LocalDateTime fromDate,
                                                             @Param("toDate") LocalDateTime toDate,
                                                             @Param("traineeName") String traineeName);
}
