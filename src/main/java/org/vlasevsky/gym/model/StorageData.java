package org.vlasevsky.gym.model;

import lombok.Data;

import java.util.List;

@Data
public class StorageData {
    private List<Trainee> trainees;
    private List<Trainer> trainers;
    private List<Training> trainings;
}