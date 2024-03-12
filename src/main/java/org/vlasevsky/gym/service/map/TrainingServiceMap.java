package org.vlasevsky.gym.service.map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.vlasevsky.gym.dao.TrainingDAO;
import org.vlasevsky.gym.exceptions.TrainingNotFoundException;
import org.vlasevsky.gym.model.Training;
import org.vlasevsky.gym.service.TrainingService;

@Service
public class TrainingServiceMap implements TrainingService {

    @Autowired
    TrainingDAO trainingDAO;

    @Override
    public Training findById(Long id) {
        return trainingDAO.findById(id).orElseThrow(() -> new TrainingNotFoundException(id));
    }

    @Override
    public Training save(Training training) {
        return trainingDAO.save(training);
    }
}
