package com.example.workloadservice.service;

import com.example.workloadservice.dto.TrainerWorkloadRequest;
import com.example.workloadservice.model.TrainerWorkloadSummary;
import jakarta.validation.Valid;

public interface TrainerWorkloadService {

    void updateWorkload(TrainerWorkloadRequest request);
    TrainerWorkloadSummary getWorkload(String username, int year, int month);
}
