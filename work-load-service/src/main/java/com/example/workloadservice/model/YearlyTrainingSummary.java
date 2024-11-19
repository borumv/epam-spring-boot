package com.example.workloadservice.model;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;




@Data
public class YearlyTrainingSummary {

    private Integer trainingYear;
    private Map<Integer, Integer> monthlySummary = new HashMap<>();
}