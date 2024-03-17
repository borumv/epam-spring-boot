package org.vlasevsky.gym.service.map;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.vlasevsky.gym.dao.TraineeDAO;
import org.vlasevsky.gym.exceptions.TraineeNotFoundException;
import org.vlasevsky.gym.model.Trainee;
import org.vlasevsky.gym.service.TraineeService;

@Slf4j
@Service
public class TraineeServiceMap implements TraineeService {

    @Autowired
    private TraineeDAO traineeDAO;

    @Override
    public Trainee findById(Long id) {
        log.info("Finding trainee by ID: {}", id);
        return traineeDAO.findById(id).orElseThrow(() -> new TraineeNotFoundException(id));
    }

    @Override
    public Trainee save(Trainee trainee) {
        log.info("Saving trainee: {}", trainee);
        return traineeDAO.save(trainee);
    }

    @Override
    public void delete(Trainee trainee) {
        log.info("Deleting trainee: {}", trainee);
        traineeDAO.delete(trainee);
    }
}