package com.vlasevsky.gym.dto;

import com.vlasevsky.gym.model.TrainingType;

import java.util.List;

public record TrainerProfileReadDto(String username,
                                    String firstName,
                                    String lastName,
                                    List<TrainingType.Type> specialization,
                                    List<TraineeReadDto> traineesList) {
}
