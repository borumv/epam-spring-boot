package com.vlasevsky.gym.repository;

import com.vlasevsky.gym.model.Trainer;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface TrainerRepository {

    //@EntityGraph(attributePaths = {"specializations", "trainees"})
    List<Trainer> findAll();

    //@EntityGraph(attributePaths = {"specializations", "trainees"})
    Optional<Trainer> findByUsername(String username);

   // @EntityGraph(attributePaths = {"specializations"})

    Set<Trainer> findTrainersNotAssignedToTrainee(@Param("traineeUsername") String traineeUsername);


    Set<Trainer> findAllTrainersByUsername(@Param("names") List<String> names);

    void save(Trainer trainer);
}
