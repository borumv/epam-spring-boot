package com.vlasevsky.gym.repository.mockbeans;

import com.vlasevsky.gym.model.Trainee;
import com.vlasevsky.gym.model.Training;
import com.vlasevsky.gym.repository.TraineeRepository;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Component
public class TraineeRepositoryImpl implements TraineeRepository {
    @Override
    public Optional<Trainee> findByUsername(String username) {
        return Optional.empty();
    }

    @Override
    public List<Training> findTraineeTrainingsByUsernameAndCriteria(String username, Date fromDate, Date toDate, String trainerName, String trainingType) {
        return null;
    }

    @Override
    public void save(Trainee trainee) {

    }

    @Override
    public void deleteById(Long id) {

    }
}
