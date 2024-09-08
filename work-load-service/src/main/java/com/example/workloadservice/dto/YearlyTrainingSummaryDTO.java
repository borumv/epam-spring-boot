package com.example.workloadservice.dto;

import lombok.Data;

import java.util.Map;

@Data
public class YearlyTrainingSummaryDTO {
    private Integer trainingYear;
    private Map<Integer, Integer> monthlySummary;
}
