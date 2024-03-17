package org.vlasevsky.gym.service.map;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.vlasevsky.gym.dao.TrainerDAO;
import org.vlasevsky.gym.exceptions.TrainerNotFoundException;
import org.vlasevsky.gym.model.Trainer;
import org.vlasevsky.gym.service.TrainerService;

@Slf4j
@Service
public class TrainerServiceMap implements TrainerService {

    @Autowired
    private TrainerDAO trainerDAO;

    @Override
    public Trainer findById(Long id) {
        log.info("Finding trainer by ID: {}", id);
        return trainerDAO.findById(id).orElseThrow(() -> new TrainerNotFoundException(id));
    }

    @Override
    public Trainer save(Trainer trainer) {
        log.info("Saving trainer: {}", trainer);
        return trainerDAO.save(trainer);
    }
}