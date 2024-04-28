package org.vlasevsky.gym.service;

import org.vlasevsky.gym.dto.*;
import org.vlasevsky.gym.model.Trainer;

import java.time.LocalDateTime;
import java.util.List;

public interface TrainerService extends BaseService<Trainer, Long>{
    CredentialsDto register(TrainerRegistrationDto registrationDto);

    TrainerProfileReadDto findTrainerByUsername(String username);

    TrainerProfileReadDto update(TrainerCreateDto dto);


    void changeActiveStatus(String username, boolean isActive);

    List<TrainingReadDto> getTrainerTrainings(String username, LocalDateTime from, LocalDateTime to, String traineeName);
}
