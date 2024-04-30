package org.vlasevsky.gym.service;

import org.vlasevsky.gym.dto.*;
import org.vlasevsky.gym.model.Trainer;

import java.time.LocalDateTime;
import java.util.List;

public interface TrainerService extends BaseService<Trainer, Long>{
    CredentialsDto register(TrainerRegistrationDto registrationDto);

    TrainerProfileReadDto findTrainerByUsername(String username);

    TrainerProfileReadDto update(String username, TrainerCreateDto dto);

    List<TrainerReadDto> findAll();
    void changeActiveStatus(String username, StatusUpdateDto dto);

    List<TrainingReadDto> getTrainerTrainings(String username, LocalDateTime from, LocalDateTime to, String traineeName);

    List<TrainerReadDto> getTrainersNotAssignedToTrainee(String traineeUsername);
}
