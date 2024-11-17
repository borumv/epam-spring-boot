package com.vlasevsky.gym.dto;

import lombok.Data;

import java.util.Map;

@Data
public class YearlyTrainingSummary {

    private Integer trainingYear;
    private Map<Integer, Integer> monthlySummary;
}
