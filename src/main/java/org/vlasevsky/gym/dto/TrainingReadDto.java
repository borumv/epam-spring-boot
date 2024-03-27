package org.vlasevsky.gym.dto;

import java.util.Date;

public record TrainingReadDto(
        Long id,
        String name,
        Date date,
        int duration,
        TraineeReadDto trainee,
        TrainerReadDto trainer
) {
}
