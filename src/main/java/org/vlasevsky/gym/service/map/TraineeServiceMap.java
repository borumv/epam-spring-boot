package org.vlasevsky.gym.service.map;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.vlasevsky.gym.dao.TraineeRepository;
import org.vlasevsky.gym.dao.TrainerRepository;
import org.vlasevsky.gym.dto.*;
import org.vlasevsky.gym.exceptions.AuthenticationException;
import org.vlasevsky.gym.exceptions.TraineeNotFoundException;
import org.vlasevsky.gym.exceptions.UserNotFoundException;
import org.vlasevsky.gym.mapper.*;
import org.vlasevsky.gym.model.Trainee;
import org.vlasevsky.gym.model.Trainer;
import org.vlasevsky.gym.model.Training;
import org.vlasevsky.gym.service.TraineeService;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class TraineeServiceMap implements TraineeService {


    private final TrainingReadMapper trainingReadMapper;
    private final TraineeCreateMapper traineeCreateMapper;
    private final TraineeReadMapper traineeReadMapper;
    private final TrainerReadMapper trainerReadMapper;
    private final TraineeRepository traineeRepository;
    private final TrainerRepository trainerRepository;
    private final UserCredentialsService userCredentialsService;



    public <T> Optional<T> findById(Long id, Mapper<Trainee, T> mapper, CredentialsDto credentialsDto) {
        if (userCredentialsService.checkCredentials(credentialsDto)) {
            throw new AuthenticationException("Invalid credentials");
        }
        return traineeRepository.findById(id)
                .map(mapper::mapFrom);
    }
    public TraineeReadDto findById(Long id, CredentialsDto credentialsDto) {

        log.info("Finding trainee by ID: {}", id);
        return findById(id, traineeReadMapper, credentialsDto).orElseThrow(() -> new TraineeNotFoundException(id));
    }


    public void delete(Trainee trainee, CredentialsDto credentialsDto) {
        if (userCredentialsService.checkCredentials(credentialsDto)) {
            throw new AuthenticationException("Invalid credentials");
        }
        log.info("Deleting trainee: {}", trainee);
        traineeRepository.delete(trainee.getId());
    }


    public CredentialsDto create(TraineeCreateAndUpdateDto trainee) {
        Trainee traineeEntity = traineeCreateMapper.mapFrom(trainee);
        String username = userCredentialsService.generateUsername(trainee.firstName(), trainee.lastName());
        String password = userCredentialsService.generateRandomPassword();
        traineeEntity.setUsername(username);
        traineeEntity.setPassword(password);
        traineeRepository.save(traineeEntity);

        return new CredentialsDto(username, password);
    }

    public void changeTraineePassword(Long traineeId, String newPassword, CredentialsDto credentialsDto) {
        if (userCredentialsService.checkCredentials(credentialsDto)) {
            throw new AuthenticationException("Invalid credentials");
        }
        userCredentialsService.changePassword(traineeId, newPassword);
    }

    public TraineeReadDto findTraineeByUsername(String username, CredentialsDto credentialsDto) {
        if (userCredentialsService.checkCredentials(credentialsDto)) {
            throw new AuthenticationException("Invalid credentials");
        }
        log.info("Finding trainee by username: {}", username);
        return traineeRepository.findByUsername(username)
                .map(traineeReadMapper::mapFrom)
                .orElseThrow(() -> new UserNotFoundException(username));
    }


    public void activateTrainee(Long id, CredentialsDto credentialsDto) {
        if (userCredentialsService.checkCredentials(credentialsDto)) {
            throw new AuthenticationException("Invalid credentials");
        }
        Trainee trainee = traineeRepository.findById(id)
                .orElseThrow(() -> new TraineeNotFoundException(id));
        if (!trainee.getIsActive()) {
            trainee.setIsActive(true);
            traineeRepository.update(trainee);
        }
    }

    public void deactivateTrainee(Long id, CredentialsDto credentialsDto) {
        if (userCredentialsService.checkCredentials(credentialsDto)) {
            throw new AuthenticationException("Invalid credentials");
        }
        Trainee trainee = traineeRepository.findById(id)
                .orElseThrow(() -> new TraineeNotFoundException(id));
        if (trainee.getIsActive()) {
            trainee.setIsActive(false);
            traineeRepository.update(trainee);
        }
    }

    public void deleteTraineeByUsername(String username, CredentialsDto credentialsDto) {
        if (userCredentialsService.checkCredentials(credentialsDto)) {
            throw new AuthenticationException("Invalid credentials");
        }
        log.info("Deleting trainee with username: {}", username);
        Optional<Trainee> traineeOpt = traineeRepository.findByUsername(username);
        if (traineeOpt.isPresent()) {
            Trainee trainee = traineeOpt.get();
            traineeRepository.delete(trainee.getId());
        } else {
            throw new UserNotFoundException(username);
        }
    }

    public List<TrainingReadDto> getTraineeTrainings(String username, Date fromDate, Date toDate, String trainerName, String trainingType, CredentialsDto credentialsDto) {
        if (userCredentialsService.checkCredentials(credentialsDto)) {
            throw new AuthenticationException("Invalid credentials");
        }
        List<Training> trainings = traineeRepository.findTraineeTrainingsByUsernameAndCriteria(username, fromDate, toDate, trainerName, trainingType);
        return trainings.stream()
                .map(trainingReadMapper::mapFrom)
                .collect(Collectors.toList());
    }

    public void updateTraineeTrainers(Long traineeId, Set<Trainer> trainers, CredentialsDto credentialsDto) {
        if (userCredentialsService.checkCredentials(credentialsDto)) {
            throw new AuthenticationException("Invalid credentials");
        }
        Trainee trainee = traineeRepository.findById(traineeId)
                .orElseThrow(() -> new TraineeNotFoundException(traineeId));
        trainee.setTrainers(trainers);
        traineeRepository.update(trainee);
    }

    public List<TrainerReadDto> getTrainersNotAssignedToTrainee(String traineeUsername, CredentialsDto credentialsDto) {
        if (userCredentialsService.checkCredentials(credentialsDto)) {
            throw new AuthenticationException("Invalid credentials");
        }
        List<Trainer> trainers = trainerRepository.findTrainersNotAssignedToTrainee(traineeUsername);
        return trainers.stream()
                .map(trainerReadMapper::mapFrom)
                .collect(Collectors.toList());
    }
}