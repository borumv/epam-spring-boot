package com.vlasevsky.gym.repository.mockbeans;

import com.vlasevsky.gym.model.Trainer;
import com.vlasevsky.gym.repository.TrainerRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.Set;
@Component
public class TrainerRepositoryImpl implements TrainerRepository {
    @Override
    public List<Trainer> findAll() {
        return null;
    }

    @Override
    public Optional<Trainer> findByUsername(String username) {
        return Optional.empty();
    }

    @Override
    public Set<Trainer> findTrainersNotAssignedToTrainee(String traineeUsername) {
        return null;
    }

    @Override
    public Set<Trainer> findAllTrainersByUsername(List<String> names) {
        return null;
    }

    @Override
    public void save(Trainer trainer) {

    }
}
