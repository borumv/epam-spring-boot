package org.vlasevsky.gym.service;

import org.vlasevsky.gym.model.Trainee;

public interface TraineeService extends BaseService<Trainee, Long> {
    void delete(Trainee object);
    void deleteById(Trainee id);
}
