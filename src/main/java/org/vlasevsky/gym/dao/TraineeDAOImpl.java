package org.vlasevsky.gym.dao;

import org.springframework.stereotype.Component;
import org.vlasevsky.gym.model.Trainee;

@Component
public class TraineeDAOImpl extends BaseAbstractDAO<Trainee> implements TraineeDAO {

    @Override
    public void delete(Trainee entity) {
        storage.getAllData().entrySet()
                .removeIf(item -> item.getValue().equals(entity));
    }
}
