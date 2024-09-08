package com.example.workloadservice.model;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.HashMap;
import java.util.Map;



@Data
public class YearlyTrainingSummary {

    @Field("training_year")
    private Integer trainingYear;

    @Field("monthly_summary")
    private Map<Integer, Integer> monthlySummary = new HashMap<>();
}