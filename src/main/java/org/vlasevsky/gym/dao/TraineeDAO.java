package org.vlasevsky.gym.dao;

import org.vlasevsky.gym.model.Trainee;

public interface TraineeDAO extends BaseDao<Trainee>{

    void delete(Trainee entity);
}
