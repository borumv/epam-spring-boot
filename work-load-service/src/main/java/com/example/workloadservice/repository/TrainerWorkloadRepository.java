package com.example.workloadservice.repository;

import com.example.workloadservice.model.TrainerWorkloadSummary;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TrainerWorkloadRepository extends JpaRepository<TrainerWorkloadSummary, Long> {
    TrainerWorkloadSummary findByUsername(String username);
}