package com.vlasevsky.gym.repository;

import com.vlasevsky.gym.model.TrainingType;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface TrainingTypeRepository {

    List<TrainingType> findAll();

    Optional<TrainingType> findByName(TrainingType.Type name);

    Set<TrainingType> findByNames(@Param("types") List<TrainingType.Type> types);
}
