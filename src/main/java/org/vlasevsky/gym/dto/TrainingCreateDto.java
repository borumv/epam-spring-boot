package org.vlasevsky.gym.dto;

import org.vlasevsky.gym.model.TrainingType;

public record TrainingCreateDto(
        String traineeUsername,
        String trainerUsername,
        String name,
        TrainingType.Type type,
        int duration
) {
}
