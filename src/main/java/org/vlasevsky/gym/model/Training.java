package org.vlasevsky.gym.model;

import lombok.Data;

import java.util.Date;

@Data
public class Training extends BaseEntity{
    private String name;
    private Date date;
    private int duration;
    private Long trainee;
    private Long trainer;
    private TrainingType trainingType;
}
