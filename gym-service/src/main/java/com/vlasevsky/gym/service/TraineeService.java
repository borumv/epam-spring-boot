package com.vlasevsky.gym.service;


import com.vlasevsky.gym.dto.*;
import com.vlasevsky.gym.model.Trainee;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public interface TraineeService extends BaseService<Trainee, Long> {


    TraineeProfileReadDto findTraineeByUsername(String username);

    TraineeProfileReadDto update(String username, TraineeCreateAndUpdateDto dto);

    void delete(String username);

    void changeActiveStatus(String username, StatusUpdateDto statusUpdateDto);

    Set<TrainingReadDto> getTraineeTrainings(String username, LocalDateTime from, LocalDateTime to, String trainerName);

    TraineeProfileReadDto updateTraineeTrainers(String username, List<String> trainers);
}
