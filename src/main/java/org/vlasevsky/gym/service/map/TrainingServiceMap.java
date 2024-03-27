package org.vlasevsky.gym.service.map;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.vlasevsky.gym.dao.TrainingRepository;
import org.vlasevsky.gym.dto.CredentialsDto;
import org.vlasevsky.gym.exceptions.AuthenticationException;
import org.vlasevsky.gym.exceptions.TrainingNotFoundException;
import org.vlasevsky.gym.mapper.Mapper;
import org.vlasevsky.gym.mapper.TrainingReadMapper;
import org.vlasevsky.gym.model.Training;
import org.vlasevsky.gym.service.TrainingService;
import org.vlasevsky.gym.dto.TrainingReadDto;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class TrainingServiceMap implements TrainingService {

    private final TrainingRepository trainingRepository;
    private final TrainingReadMapper trainingReadMapper;
    private final UserCredentialsService userCredentialsService;

    public <T> Optional<T> findById(Long id, Mapper<Training, T> mapper, CredentialsDto credentialsDto) {
        if (userCredentialsService.checkCredentials(credentialsDto)) {
            throw new AuthenticationException("Invalid credentials");
        }
        return trainingRepository.findById(id)
                .map(mapper::mapFrom);
    }

    public TrainingReadDto findById(Long id, CredentialsDto credentialsDto) {
        log.info("Finding training by ID: {}", id);
        return findById(id, trainingReadMapper, credentialsDto)
                .orElseThrow(() -> new TrainingNotFoundException(id));
    }

    public Training create(Training training, CredentialsDto credentialsDto) {
        if (userCredentialsService.checkCredentials(credentialsDto)) {
            throw new AuthenticationException("Invalid credentials");
        }
        log.info("Saving training: {}", training);
        return trainingRepository.save(training);
    }

    public boolean delete(Long id, CredentialsDto credentialsDto) {
        if (userCredentialsService.checkCredentials(credentialsDto)) {
            throw new AuthenticationException("Invalid credentials");
        }
        Optional<Training> maybeTraining = trainingRepository.findById(id);
        maybeTraining.ifPresent(training -> trainingRepository.delete(training.getId()));
        return maybeTraining.isPresent();
    }
}
