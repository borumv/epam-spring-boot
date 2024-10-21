package com.vlasevsky.gym.repository;

import com.vlasevsky.gym.model.Trainer;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface TrainerRepository extends JpaRepository<Trainer, Long> {

    //@EntityGraph(attributePaths = {"specializations", "trainees"})
    List<Trainer> findAll();

    //@EntityGraph(attributePaths = {"specializations", "trainees"})
    Optional<Trainer> findByUsername(String username);

   // @EntityGraph(attributePaths = {"specializations"})
    @Query("SELECT t FROM Trainer t WHERE t NOT IN " +
            "(SELECT tr.trainers FROM Trainee tr WHERE tr.username = :traineeUsername)")
    Set<Trainer> findTrainersNotAssignedToTrainee(@Param("traineeUsername") String traineeUsername);

    @EntityGraph(attributePaths = {"specializations", "trainees"})
    @Query("SELECT t FROM Trainer t WHERE t.username IN (:names)")
    Set<Trainer> findAllTrainersByUsername(@Param("names") List<String> names);
}
