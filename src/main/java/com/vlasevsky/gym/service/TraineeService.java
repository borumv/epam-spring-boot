package com.vlasevsky.gym.service;


import com.vlasevsky.gym.dto.*;
import com.vlasevsky.gym.model.Trainee;

import java.time.LocalDateTime;
import java.util.List;

public interface TraineeService extends BaseService<Trainee, Long> {


    CredentialsDto register(TraineeRegistrationDto registrationDto);

    TraineeProfileReadDto findTraineeByUsername(String username);

    TraineeProfileReadDto update(String username, TraineeCreateAndUpdateDto dto);

    void delete(String username);

    void changeActiveStatus(String username, StatusUpdateDto statusUpdateDto);

    List<TrainingReadDto> getTraineeTrainings(String username, LocalDateTime from, LocalDateTime to, String trainerName);

    TraineeProfileReadDto updateTraineeTrainers(String username, List<String> trainers);
}
