package org.vlasevsky.gym.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.vlasevsky.gym.model.Trainee;

@Component
@Slf4j
public class TraineeDAOImpl extends BaseAbstractDAO<Trainee> implements TraineeDAO {

    @Override
    public void delete(Trainee entity) {
        storage.delete(entity.getId());
        log.info("Trainee deleted: {}", entity);
    }
}
