package org.vlasevsky.gym.service.map;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.vlasevsky.gym.dao.TraineeRepository;
import org.vlasevsky.gym.dao.TrainerRepository;
import org.vlasevsky.gym.dao.TrainingRepository;
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

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class TraineeServiceMap implements TraineeService {

    private final TraineeRepository traineeRepository;
    private final TrainerRepository trainerRepository;
    private final TrainingRepository trainingRepository;
    private final UserCredentialsService userCredentialsService;
    private final TraineeMapper traineeMapper;
    private final TrainingMapper trainingMapper;
    private final TrainerMapper trainerMapper;

    @Transactional
    public void delete(String username) {
        log.info("Deleting trainee: {}", username);
        Trainee trainee = traineeRepository.findByUsername(username)
                .orElseThrow(() -> new TraineeNotFoundException(username));

        // Remove associations with trainers
        trainee.getTrainers().forEach(trainer -> {
            trainer.getTrainees().remove(trainee);
        });
        // Remove associated trainings
        trainee.getTrainings().clear();
        // Now it's safe to delete the trainee
        traineeRepository.delete(trainee.getId());
    }

    @Transactional
    @Override
    public void changeActiveStatus(String username, boolean isActive) {
        Trainee trainee = traineeRepository.findByUsername(username)
                .orElseThrow(() -> new TraineeNotFoundException(username));
        trainee.setIsActive(isActive);
        traineeRepository.save(trainee);
    }

    @Transactional
    @Override
    public List<TrainingReadDto> getTraineeTrainings(String trainerUsername, LocalDateTime from, LocalDateTime to, String traineeName) {
        List<Training> trainings = trainingRepository.findTrainingsByTraineeAndPeriodAndTrainer(trainerUsername, from, to, traineeName);
        return trainingMapper.toDTOList(trainings);
    }

    @Transactional
    @Override
    public CredentialsDto register(TraineeRegistrationDto registrationDto) {
        Trainee traineeEntity = traineeMapper.toEntity(registrationDto);
        String username = userCredentialsService.generateUsername(traineeEntity.getFirstName(), traineeEntity.getLastName());
        String password = userCredentialsService.generateRandomPassword();
        traineeEntity.setUsername(username);
        traineeEntity.setPassword(password);
        traineeRepository.save(traineeEntity);

        return new CredentialsDto(username, password);
    }

    @Transactional
    public TraineeProfileReadDto update(TraineeCreateAndUpdateDto dto) {

        Trainee existingTrainee = traineeRepository.findByUsername(dto.username())
                .orElseThrow(() -> new UserNotFoundException("User with username: " + dto.username() + " not found"));

        existingTrainee.setFirstName(dto.firstName());
        existingTrainee.setLastName(dto.lastName());
        existingTrainee.setAddress(dto.address());
        existingTrainee.setDateOfBirth(dto.dateOfBirth());
        existingTrainee.setIsActive(dto.isActive());


        traineeRepository.save(existingTrainee);


        List<TrainerReadDto> trainers = trainerMapper.toDTOList(existingTrainee.getTrainers());
        TraineeProfileReadDto updatedProfile = traineeMapper.toTraineeProfileDto(existingTrainee);
        updatedProfile.trainers().addAll(trainers);

        return updatedProfile;
    }

    @Transactional
    public void changeTraineePassword(Long traineeId, String newPassword, CredentialsDto credentialsDto) {
        if (userCredentialsService.checkCredentials(credentialsDto)) {
            throw new AuthenticationException("Invalid credentials");
        }
        userCredentialsService.changePassword(traineeId, newPassword);
    }

    @Transactional
    @Override
    public TraineeProfileReadDto findTraineeByUsername(String username) {
        log.info("Finding trainee by username: {}", username);
        Trainee trainee = traineeRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));


        List<TrainerReadDto> trainers = trainerMapper.toDTOList(trainee.getTrainers());
        TraineeProfileReadDto traineeProfileReadDto = traineeMapper.toTraineeProfileDto(trainee);
        traineeProfileReadDto.trainers().addAll(trainers);
        return traineeProfileReadDto;
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
    @Override
    public TraineeProfileReadDto updateTraineeTrainers(String username, List<String> trainers) {
        Trainee trainee = traineeRepository.findByUsername(username)
                .orElseThrow(() -> new TraineeNotFoundException(username));

        List<Trainer> trainersByUsername = trainerRepository.findAllTrainersByUsername(trainers);
        trainee.setTrainers(trainersByUsername);
        traineeRepository.save(trainee);

        return findTraineeByUsername(username);
    }

    @Transactional
    @Override
    public List<TrainerReadDto> getTrainersNotAssignedToTrainee(String traineeUsername) {
        List<Trainer> trainers = trainerRepository.findTrainersNotAssignedToTrainee(traineeUsername);
        return trainerMapper.toDTOList(trainers);
    }
}