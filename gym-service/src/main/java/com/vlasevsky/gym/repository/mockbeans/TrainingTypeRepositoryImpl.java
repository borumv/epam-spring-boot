package com.vlasevsky.gym.repository.mockbeans;

import com.vlasevsky.gym.model.TrainingType;
import com.vlasevsky.gym.repository.TrainingTypeRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.Set;
@Component
public class TrainingTypeRepositoryImpl implements TrainingTypeRepository {
    @Override
    public List<TrainingType> findAll() {
        return null;
    }

    @Override
    public Optional<TrainingType> findByName(TrainingType.Type name) {
        return Optional.empty();
    }

    @Override
    public Set<TrainingType> findByNames(List<TrainingType.Type> types) {
        return null;
    }
}
