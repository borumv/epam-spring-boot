package org.vlasevsky.gym.model;

import java.util.Date;

public class Training {
    private Long id;
    private String name;
    private Date date;
    private int duration;
    private Trainee trainee;
    private Trainer trainer;
    private TrainingType trainingType;
}
