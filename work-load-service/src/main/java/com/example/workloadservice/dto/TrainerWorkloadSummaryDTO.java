package com.example.workloadservice.dto;

import lombok.Data;

import java.util.List;

@Data
public class TrainerWorkloadSummaryDTO {
    private String id;
    private String username;
    private String firstName;
    private String lastName;
    private Boolean isActive;
    private List<YearlyTrainingSummaryDTO> yearlySummaries;
}
