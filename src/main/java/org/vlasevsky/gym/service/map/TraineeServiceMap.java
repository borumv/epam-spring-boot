package org.vlasevsky.gym.service.map;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import org.vlasevsky.gym.dao.TraineeRepository;
import org.vlasevsky.gym.dao.TrainerRepository;
import org.vlasevsky.gym.dto.*;
import org.vlasevsky.gym.exceptions.AuthenticationException;
import org.vlasevsky.gym.exceptions.TraineeNotFoundException;
import org.vlasevsky.gym.exceptions.UserNotFoundException;
import org.vlasevsky.gym.mapper.mapstruct.TraineeMapper;
import org.vlasevsky.gym.mapper.mapstruct.TrainerMapper;
import org.vlasevsky.gym.mapper.mapstruct.TrainingMapper;
import org.vlasevsky.gym.model.Trainee;
import org.vlasevsky.gym.model.Trainer;
import org.vlasevsky.gym.model.Training;
import org.vlasevsky.gym.service.TraineeService;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Service
@AllArgsConstructor
public class TraineeServiceMap implements TraineeService {

    private final TraineeRepository traineeRepository;
    private final TrainerRepository trainerRepository;
    private final UserCredentialsService userCredentialsService;
    private final TraineeMapper traineeMapper;
    private final TrainingMapper trainingMapper;
    private final TrainerMapper trainerMapper;


    public TraineeReadDto findById(Long id, CredentialsDto credentialsDto) {

        log.info("Finding trainee by ID: {}", id);
        Trainee trainee = traineeRepository.findById(id)
                .orElseThrow(() -> new TraineeNotFoundException(id));
        return traineeMapper.toDto(trainee);
    }


    @Transactional
    public void delete(Trainee trainee, CredentialsDto credentialsDto) {
        if (userCredentialsService.checkCredentials(credentialsDto)) {
            throw new AuthenticationException("Invalid credentials");
        }
        log.info("Deleting trainee: {}", trainee);
        traineeRepository.delete(trainee.getId());
    }

    @Transactional
    public CredentialsDto create(TraineeCreateAndUpdateDto trainee) {
        Trainee traineeEntity = traineeMapper.toEntity(trainee);
        String username = userCredentialsService.generateUsername(trainee.firstName(), trainee.lastName());
        String password = userCredentialsService.generateRandomPassword();
        traineeEntity.setUsername(username);
        traineeEntity.setPassword(password);
        traineeRepository.save(traineeEntity);

        return new CredentialsDto(username, password);
    }
    @Transactional
    public void changeTraineePassword(Long traineeId, String newPassword, CredentialsDto credentialsDto) {
        if (userCredentialsService.checkCredentials(credentialsDto)) {
            throw new AuthenticationException("Invalid credentials");
        }
        userCredentialsService.changePassword(traineeId, newPassword);
    }
    @Transactional
    public TraineeReadDto findTraineeByUsername(String username, CredentialsDto credentialsDto) {
        log.info("Finding trainee by username: {}", username);
        Trainee trainee = traineeRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));
        return traineeMapper.toDto(trainee);
    }
    @Transactional
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
    @Transactional
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
    @Transactional
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
    @Transactional
    public List<TrainingReadDto> getTraineeTrainings(String username, Date fromDate, Date toDate, String trainerName, String trainingType, CredentialsDto credentialsDto) {
        if (userCredentialsService.checkCredentials(credentialsDto)) {
            throw new AuthenticationException("Invalid credentials");
        }
        List<Training> trainings = traineeRepository.findTraineeTrainingsByUsernameAndCriteria(username, fromDate, toDate, trainerName, trainingType);
        return trainingMapper.toDTOList(trainings);
    }
    @Transactional
    public void updateTraineeTrainers(Long traineeId, Set<Trainer> trainers, CredentialsDto credentialsDto) {
        if (userCredentialsService.checkCredentials(credentialsDto)) {
            throw new AuthenticationException("Invalid credentials");
        }
        Trainee trainee = traineeRepository.findById(traineeId)
                .orElseThrow(() -> new TraineeNotFoundException(traineeId));
        trainee.setTrainers(trainers);
        traineeRepository.update(trainee);
    }
    @Transactional
    public List<TrainerReadDto> getTrainersNotAssignedToTrainee(String traineeUsername, CredentialsDto credentialsDto) {
        if (userCredentialsService.checkCredentials(credentialsDto)) {
            throw new AuthenticationException("Invalid credentials");
        }
        List<Trainer> trainers = trainerRepository.findTrainersNotAssignedToTrainee(traineeUsername);
        return trainerMapper.toDTOList(trainers);
    }
}