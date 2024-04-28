package org.vlasevsky.gym.service.map;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.vlasevsky.gym.dao.TraineeRepository;
import org.vlasevsky.gym.dao.TrainerRepository;
import org.vlasevsky.gym.dao.TrainingRepository;
import org.vlasevsky.gym.dao.TrainingTypeRepository;
import org.vlasevsky.gym.dto.CredentialsDto;
import org.vlasevsky.gym.dto.TrainingCreateDto;
import org.vlasevsky.gym.exceptions.AuthenticationException;
import org.vlasevsky.gym.exceptions.TraineeNotFoundException;
import org.vlasevsky.gym.exceptions.TrainerNotFoundException;
import org.vlasevsky.gym.exceptions.TrainingTypeNotFoundException;
import org.vlasevsky.gym.mapper.mapstruct.TrainingMapper;
import org.vlasevsky.gym.model.Trainee;
import org.vlasevsky.gym.model.Trainer;
import org.vlasevsky.gym.model.Training;
import org.vlasevsky.gym.model.TrainingType;
import org.vlasevsky.gym.service.TrainingService;

import java.util.Date;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class TrainingServiceMap implements TrainingService {
    private final TrainingRepository trainingRepository;
    private final UserCredentialsService userCredentialsService;
    private final TrainingMapper trainingMapper;

    private final TraineeRepository traineeRepository;

    private final TrainerRepository trainerRepository;

    private final TrainingTypeRepository trainingTypeRepository;

    @Transactional
    public Training create(Training training, CredentialsDto credentialsDto) {
        if (userCredentialsService.checkCredentials(credentialsDto)) {
            throw new AuthenticationException("Invalid credentials");
        }
        log.info("Saving training: {}", training);
        return trainingRepository.save(training);
    }

    @Transactional
    public boolean delete(Long id, CredentialsDto credentialsDto) {
        if (userCredentialsService.checkCredentials(credentialsDto)) {
            throw new AuthenticationException("Invalid credentials");
        }
        Optional<Training> maybeTraining = trainingRepository.findById(id);
        maybeTraining.ifPresent(training -> trainingRepository.delete(training.getId()));
        return maybeTraining.isPresent();
    }

    @Transactional
    @Override
    public void create(TrainingCreateDto createDto) {
        Trainee trainee = traineeRepository.findByUsername(createDto.traineeUsername())
                .orElseThrow(() -> new TraineeNotFoundException(createDto.traineeUsername()));
        Trainer trainer = trainerRepository.findByUsername(createDto.trainerUsername())
                .orElseThrow(() -> new TrainerNotFoundException(createDto.trainerUsername()));
        TrainingType trainingType = trainingTypeRepository.findByName(createDto.type())
                .orElseThrow(() -> new TrainingTypeNotFoundException("Training type not found"));

        Training training = trainingMapper.toEntity(createDto);
        training.setTrainee(trainee);
        training.setTrainer(trainer);
        training.setTrainingType(trainingType);
        training.setDate(new Date());
        trainingRepository.save(training);
    }
}
