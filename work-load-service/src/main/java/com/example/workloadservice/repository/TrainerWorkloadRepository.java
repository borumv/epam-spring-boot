package com.example.workloadservice.repository;

import com.example.workloadservice.model.TrainerWorkloadSummary;
import org.springframework.stereotype.Repository;


public interface TrainerWorkloadRepository {
    void save(TrainerWorkloadSummary summary);
    TrainerWorkloadSummary findByUsername(String username);
}