package com.vlasevsky.gym.dto;

import com.vlasevsky.gym.model.TrainingType;

public record TrainingCreateDto(
        String traineeUsername,
        String trainerUsername,
        String name,
        TrainingType.Type type,
        int duration
) {
}
