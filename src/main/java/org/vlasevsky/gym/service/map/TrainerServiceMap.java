package org.vlasevsky.gym.service.map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.vlasevsky.gym.dao.TrainerDAO;
import org.vlasevsky.gym.exceptions.TrainerNotFoundException;
import org.vlasevsky.gym.model.Trainer;
import org.vlasevsky.gym.service.TrainerService;

@Service
public class TrainerServiceMap implements TrainerService {

    @Autowired
    private TrainerDAO trainerDAO;

    @Override
    public Trainer findById(Long id) {
        return trainerDAO.findById(id).orElseThrow(() -> new TrainerNotFoundException(id));
    }

    @Override
    public Trainer save(Trainer trainer) {
        return trainerDAO.save(trainer);
    }
}
