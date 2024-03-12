package org.vlasevsky.gym.model;

import lombok.Data;

@Data
public class StorageData {
    private Trainee[] trainees;
    private Trainer[] trainers;
    private Training[] trainings;

}