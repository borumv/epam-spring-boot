package com.example.workloadservice.repository;

import com.example.workloadservice.model.TrainerWorkloadSummary;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TrainerWorkloadRepository extends MongoRepository<TrainerWorkloadSummary, String> {

    Optional<TrainerWorkloadSummary> findByUsername(String username);

    // Дополнительные методы поиска по имени и фамилии для индексации
    List<TrainerWorkloadSummary> findByFirstNameAndLastName(String firstName, String lastName);
}