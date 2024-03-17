package org.vlasevsky.gym.service.map;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.vlasevsky.gym.dao.TrainingDAO;
import org.vlasevsky.gym.exceptions.TrainingNotFoundException;
import org.vlasevsky.gym.model.Training;
import org.vlasevsky.gym.service.TrainingService;


@Slf4j
@Service
public class TrainingServiceMap implements TrainingService {

    @Autowired
    private TrainingDAO trainingDAO;

    @Override
    public Training findById(Long id) {
        log.info("Finding training by ID: {}", id);
        return trainingDAO.findById(id).orElseThrow(() -> new TrainingNotFoundException(id));
    }

    @Override
    public Training save(Training training) {
        log.info("Saving training: {}", training);
        return trainingDAO.save(training);
    }
}