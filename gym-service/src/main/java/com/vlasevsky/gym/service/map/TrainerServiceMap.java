package com.vlasevsky.gym.service.map;

import com.vlasevsky.gym.config.JmsConstants;
import com.vlasevsky.gym.dto.*;
import com.vlasevsky.gym.exceptions.AuthenticationException;
import com.vlasevsky.gym.exceptions.TrainerNotFoundException;
import com.vlasevsky.gym.feign.WorkLoadClient;
import com.vlasevsky.gym.mapstruct.TraineeMapper;
import com.vlasevsky.gym.mapstruct.TrainerMapper;
import com.vlasevsky.gym.mapstruct.TrainingMapper;
import com.vlasevsky.gym.model.Trainer;
import com.vlasevsky.gym.model.Training;
import com.vlasevsky.gym.model.TrainingType;
import com.vlasevsky.gym.repository.TrainerRepository;
import com.vlasevsky.gym.repository.TrainingRepository;
import com.vlasevsky.gym.repository.TrainingTypeRepository;
import com.vlasevsky.gym.service.TrainerService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class TrainerServiceMap implements TrainerService {

    private final TrainerRepository trainerRepository;
    private final TrainingRepository trainingRepository;
    private final TrainingTypeRepository trainingTypeRepository;
    private final TrainerMapper trainerMapper;
    private final TraineeMapper traineeMapper;
    private final TrainingMapper trainingMapper;

    private final WorkLoadClient workLoadClient;

    private JmsTemplate jmsTemplate;
    @Transactional
    @Override
    public TrainerProfileReadDto findTrainerByUsername(String username) {
        log.info("Finding trainer by username: {}", username);
        Trainer trainer = trainerRepository.findByUsername(username).orElseThrow(() -> new TrainerNotFoundException(username));
        if (trainer != null) {
            List<TrainingType.Type> specializations = TrainerMapper.mapSpecializations(trainer.getSpecializations());
            List<TraineeReadDto> traineesList = (trainer.getTrainees() != null && !trainer.getTrainees().isEmpty())
                    ? trainer.getTrainees().stream()
                    .map(traineeMapper::toDto)
                    .collect(Collectors.toList())
                    : Collections.emptyList();
            TrainerProfileReadDto profile = new TrainerProfileReadDto(
                    trainer.getUsername(),
                    trainer.getFirstName(),
                    trainer.getLastName(),
                    specializations,
                    traineesList
            );
            log.info("Trainer found: {}", profile);
            return profile;
        } else {
            log.warn("Trainer not found with username: {}", username);
            return null;
        }
    }

    @Transactional
    @Override
    public TrainerProfileReadDto update(String username, TrainerCreateDto dto) {
        log.info("Updating trainer with username: {}", username);
        Trainer trainer = trainerRepository.findByUsername(username)
                .orElseThrow(() -> {
                    log.warn("Trainer not found with username: {}", username);
                    return new TrainerNotFoundException("Trainer not found with username: " + username);
                });

        trainer.setFirstName(dto.firstName());
        trainer.setLastName(dto.lastName());
        trainer.setIsActive(dto.isActive());
        Set<TrainingType> specializations = trainingTypeRepository.findByNames(dto.specializations());
        trainer.setSpecializations(specializations);
        trainerRepository.save(trainer);
        log.info("Trainer with username: {} updated successfully", username);
        return findTrainerByUsername(username);
    }

    @Transactional
    @Override
    public Set<TrainerReadDto> findAll() {
        log.info("Finding all trainers");
        List<Trainer> trainers = trainerRepository.findAll();
        Set<TrainerReadDto> trainerDtos = trainerMapper.toDTOList(Set.copyOf(trainers));
        log.info("Found {} trainers", trainerDtos.size());
        return trainerDtos;
    }

    @Transactional
    @Override
    public void changeActiveStatus(String username, StatusUpdateDto dto) {
        log.info("Changing active status for trainer with username: {}", username);
        Trainer trainer = trainerRepository.findByUsername(username)
                .orElseThrow(() -> {
                    log.warn("Trainer not found with username: {}", username);
                    return new TrainerNotFoundException(username);
                });
        trainer.setIsActive(dto.isActive());
        trainerRepository.save(trainer);
        log.info("Trainer {} active status changed to {}", username, dto.isActive());
    }

    @Transactional
    @Override
    public Set<TrainingReadDto> getTrainerTrainings(String trainerUsername, LocalDateTime fromDate, LocalDateTime toDate, String traineeName) {
        log.info("Fetching trainings for trainer with username: {} from {} to {}", trainerUsername, fromDate, toDate);
        Set<Training> trainings = trainingRepository.findTrainingsByTrainerAndPeriodAndTrainee(trainerUsername, fromDate, toDate, traineeName);
        Set<TrainingReadDto> trainingDtos = trainingMapper.toDTOList(trainings);
        log.info("Found {} trainings", trainingDtos.size());
        return trainingDtos;
    }

    @Transactional
    @Override
    public Set<TrainerReadDto> getTrainersNotAssignedToTrainee(String traineeUsername) {
        log.info("Finding trainers not assigned to trainee with username: {}", traineeUsername);
        Set<Trainer> trainers = trainerRepository.findTrainersNotAssignedToTrainee(traineeUsername);
        Set<TrainerReadDto> trainerDtos = trainerMapper.toDTOList(trainers);
        log.info("Found {} trainers not assigned to trainee", trainerDtos.size());
        return trainerDtos;
    }

    public void updateTrainerWorkload(TrainerWorkloadRequest request) {
        String username = request.getUsername();
        trainerRepository.findByUsername(username)
                .orElseThrow(() -> {
                    log.warn("Trainer not found with username: {}", username);
                    return new TrainerNotFoundException(username);
                });
        jmsTemplate.convertAndSend(JmsConstants.WORKLOAD_QUEUE, request);
    }

    public TrainerWorkloadSummary getTrainerWorkload(String username, int year, int month) {
        trainerRepository.findByUsername(username)
                .orElseThrow(() -> {
                    log.warn("Trainer not found with username: {}", username);
                    return new TrainerNotFoundException(username);
                });
        return workLoadClient.getWorkload(username, year, month).getBody();
    }
}
