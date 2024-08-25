package com.example.workloadservice.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
@Entity
@Table(name = "yearly_training_summary")
public class YearlyTrainingSummary {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "training_year")
    private Integer trainingYear;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "monthly_training_summary", joinColumns = @JoinColumn(name = "yearly_summary_id"))
    @MapKeyColumn(name = "training_month")
    @Column(name = "training_duration")
    private Map<Integer, Integer> monthlySummary = new HashMap<>();
}
