package org.vlasevsky.gym.service.map;


import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.vlasevsky.gym.dao.TrainerRepository;
import org.vlasevsky.gym.dao.TrainingRepository;
import org.vlasevsky.gym.dao.TrainingTypeRepository;
import org.vlasevsky.gym.dao.UserRepository;
import org.vlasevsky.gym.dto.*;
import org.vlasevsky.gym.exceptions.AuthenticationException;
import org.vlasevsky.gym.exceptions.TrainerNotFoundException;
import org.vlasevsky.gym.mapper.mapstruct.TraineeMapper;
import org.vlasevsky.gym.mapper.mapstruct.TrainerMapper;
import org.vlasevsky.gym.mapper.mapstruct.TrainingMapper;
import org.vlasevsky.gym.model.Trainer;
import org.vlasevsky.gym.model.Training;
import org.vlasevsky.gym.model.TrainingType;
import org.vlasevsky.gym.service.TrainerService;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class TrainerServiceMap implements TrainerService {

    private final TrainerRepository trainerRepository;
    private final TrainingRepository trainingRepository;
    private final TrainingTypeRepository trainingTypeRepository;
    private final UserCredentialsService userCredentialsService;
    private final TrainerMapper trainerMapper;
    private final TraineeMapper traineeMapper;
    private final TrainingMapper trainingMapper;

    @Transactional
    @Override
    public CredentialsDto register(TrainerRegistrationDto registrationDto) {

        Trainer trainerEntity = trainerMapper.toEntity(registrationDto);
        String username = userCredentialsService.generateUsername(trainerEntity.getFirstName(), trainerEntity.getLastName());
        String password = userCredentialsService.generateRandomPassword();
        trainerEntity.setUsername(username);
        trainerEntity.setPassword(password);
        trainerRepository.save(trainerEntity);

        return new CredentialsDto(username, password);
    }

    @Transactional
    @Override
    public TrainerProfileReadDto findTrainerByUsername(String username) {
        Trainer trainer = trainerRepository.findByUsername(username).orElse(null);
        if (trainer != null) {
            List<TrainingType.Type> specializations = TrainerMapper.mapSpecializations(trainer.getSpecializations());
            List<TraineeReadDto> traineesList = (trainer.getTrainees() != null && !trainer.getTrainees().isEmpty())
                    ? trainer.getTrainees().stream()
                    .map(traineeMapper::toDto)
                    .collect(Collectors.toList())
                    : Collections.emptyList();
            return new TrainerProfileReadDto(trainer.getUsername(), trainer.getFirstName(), trainer.getLastName(),
                    specializations, traineesList);
        }
        return null;
    }

    @Transactional
    @Override
    public TrainerProfileReadDto update(TrainerCreateDto dto) {

        Trainer trainer = trainerRepository.findByUsername(dto.username())
                .orElseThrow(() -> new TrainerNotFoundException("Trainer not found with username: " + dto.username()));

        trainer.setFirstName(dto.firstName());
        trainer.setLastName(dto.lastName());
        trainer.setIsActive(dto.isActive());
        List<TrainingType> specializations = trainingTypeRepository.findByByNames(dto.specializations());
        trainer.setSpecializations(specializations);

        trainerRepository.save(trainer);


        return findTrainerByUsername(dto.username());
    }

    @Transactional
    @Override
    public void changeActiveStatus(String username, boolean isActive) {
        Trainer trainer = trainerRepository.findByUsername(username)
                .orElseThrow(() -> new TrainerNotFoundException(username));
        trainer.setIsActive(isActive);
        trainerRepository.save(trainer);
    }

    @Transactional
    @Override
    public List<TrainingReadDto> getTrainerTrainings(String trainerUsername, LocalDateTime fromDate, LocalDateTime toDate, String traineeName) {
        List<Training> trainings = trainingRepository.findTrainingsByTrainerAndPeriodAndTrainee(trainerUsername, fromDate, toDate, traineeName);
        log.info("Trainings found: {}", trainings);
        return trainingMapper.toDTOList(trainings);
    }


    @Transactional
    public void changeTrainerPassword(Long trainerId, String newPassword, CredentialsDto credentialsDto) {
        if (userCredentialsService.checkCredentials(credentialsDto)) {
            throw new AuthenticationException("Invalid credentials");
        }
        log.info("Checking password for trainer with trainer id: {}", trainerId);
        userCredentialsService.changePassword(trainerId, newPassword);
    }
}
