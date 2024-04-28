package org.vlasevsky.gym.service;

import org.vlasevsky.gym.dto.*;
import org.vlasevsky.gym.model.Trainee;

import java.time.LocalDateTime;
import java.util.List;

public interface TraineeService extends BaseService<Trainee, Long> {


    CredentialsDto register(TraineeRegistrationDto registrationDto);

    TraineeProfileReadDto findTraineeByUsername(String username);

    TraineeProfileReadDto update(TraineeCreateAndUpdateDto dto);

    void delete(String username);

    void changeActiveStatus(String username, boolean isActive);

    List<TrainingReadDto> getTraineeTrainings(String username, LocalDateTime from, LocalDateTime to, String trainerName);

    List<TrainerReadDto> getTrainersNotAssignedToTrainee(String traineeUsername);

    TraineeProfileReadDto updateTraineeTrainers(String username, List<String> trainers);
}
