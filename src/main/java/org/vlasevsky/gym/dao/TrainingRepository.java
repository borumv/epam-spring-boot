package org.vlasevsky.gym.dao;

import org.springframework.stereotype.Component;
import org.vlasevsky.gym.model.Training;

@Component
public class TrainingRepository extends BaseAbstractDAO<Long, Training> {

    public TrainingRepository() {
        super(Training.class);
    }
}
